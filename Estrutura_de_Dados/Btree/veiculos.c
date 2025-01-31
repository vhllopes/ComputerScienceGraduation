#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <unistd.h>

#define P 5  // Tamanho máximo da fila de páginas
#define M 4 // Tamanho máximo da Árvore B

#define TAMANHO_PLACA 8
#define TAMANHO_MODELO 20
#define TAMANHO_MARCA 20
#define TAMANHO_CATEGORIA 15
#define TAMANHO_STATUS 16
#define TAMANHO_REGISTRO 88 // Tamanho de um registro em bytes


// Estrutura para o nó da Árvore B
struct BTreeNode {
    int num_keys;
    char keys[M-1][8];
    int rnn[M-1];
    struct BTreeNode *children[M];
    bool is_leaf;
};

// Estrutura para o Veículo
typedef struct {
    char placa[TAMANHO_PLACA];
    char modelo[TAMANHO_MODELO];
    char marca[TAMANHO_MARCA];
    int ano;
    char categoria[TAMANHO_CATEGORIA];
    int quilometragem;
    char status[TAMANHO_STATUS];
} Veiculo;

// Estrutura para representar uma página com uma placa e um RNN
typedef struct {
    char placa[TAMANHO_PLACA];
    int rnn;
} Pagina;

// Estrutura para a fila de páginas
typedef struct {
    Pagina paginas[P];
    int tamanho;  // Número atual de páginas na fila
} FilaPagina;

// Variável global para controle do RNN
int current_rnn = 0;

// Prototipação das Funções
struct BTreeNode *createNode(bool is_leaf);
void splitChild(struct BTreeNode *parent, int index);
void insertNonFull(struct BTreeNode *node, char *key);
void insert(struct BTreeNode **root, char *key);
int search(struct BTreeNode *node, char *key);
int findMaxRNN(struct BTreeNode *root);
void saveTreeToFile(struct BTreeNode *root, FILE *file);
void lerEInserirPlacas(struct BTreeNode **root);
struct BTreeNode *loadTreeFromFile(FILE *file);
struct BTreeNode *initializeOrLoadTree(char *filename);
void salvarVeiculoNoArquivo(Veiculo *veiculo);
void removeFromBTree(struct BTreeNode *node, char *key);
void removeFromLeaf(struct BTreeNode *node, int index);
void removeFromNonLeaf(struct BTreeNode *node, int index);
char* getPredecessor(struct BTreeNode *node, int index);
char* getSuccessor(struct BTreeNode *node, int index);
void fill(struct BTreeNode *node, int index);
void borrowFromPrev(struct BTreeNode *node, int index);
void borrowFromNext(struct BTreeNode *node, int index);
void merge(struct BTreeNode *node, int index);
void removeVeiculoDoArquivo(int rnn);

// Função para inicializar a fila
void inicializarFila(FilaPagina *fila) {
    fila->tamanho = 0;
}

// Função para verificar se a fila está cheia
bool filaCheia(FilaPagina *fila) {
    return fila->tamanho == P;
}

// Função para adicionar uma página à fila
void adicionarPagina(FilaPagina *fila, const char *placa, int rnn) {
    // Se a fila está cheia, remove a página menos recentemente usada (a primeira)
    if (filaCheia(fila)) {
        for (int i = 1; i < P; i++) {
            fila->paginas[i - 1] = fila->paginas[i];
        }
        fila->tamanho--;
    }

    // Adiciona a nova página no final da fila
    strncpy(fila->paginas[fila->tamanho].placa, placa, TAMANHO_PLACA - 1);
    fila->paginas[fila->tamanho].placa[TAMANHO_PLACA - 1] = '\0';  // Garante a terminação
    fila->paginas[fila->tamanho].rnn = rnn;
    fila->tamanho++;
}

// Função para buscar uma página na fila
int buscarPagina(FilaPagina *fila, const char *placa) {
    for (int i = 0; i < fila->tamanho; i++) {
        if (strcmp(fila->paginas[i].placa, placa) == 0) {
            // Se encontrada, move a página para o final da fila (acesso recente)
            Pagina pagina = fila->paginas[i];
            for (int j = i; j < fila->tamanho - 1; j++) {
                fila->paginas[j] = fila->paginas[j + 1];
            }
            fila->paginas[fila->tamanho - 1] = pagina;
            return pagina.rnn;  // Retorna o RNN da página encontrada
        }
    }
    return -1;  // Retorna -1 se a página não está na fila
}

void removerPagina(FilaPagina *fila, const char *placa) {
    int i;
    // Procura pela página com a placa fornecida
    for (i = 0; i < fila->tamanho; i++) {
        if (strcmp(fila->paginas[i].placa, placa) == 0) {
            break;
        }
    }

    // Se a placa foi encontrada, removemos a página
    if (i < fila->tamanho) {
        // Ajusta a fila, movendo as páginas subsequentes uma posição para frente
        for (int j = i; j < fila->tamanho - 1; j++) {
            fila->paginas[j] = fila->paginas[j + 1];
        }
        fila->tamanho--;  // Reduz o tamanho da fila
    } else {
        printf("Página com placa %s não encontrada na fila.\n", placa);
    }
}

// Função de criação de um nó
// Cria um novo nó para a árvore B e define se é folha ou não.
// Inicializa o número de chaves e define todos os ponteiros de filhos como NULL.
struct BTreeNode *createNode(bool is_leaf) {
    struct BTreeNode *newNode = (struct BTreeNode *)malloc(sizeof(struct BTreeNode));
    if (newNode == NULL) {
        perror("Falha na alocação de memória");
        exit(EXIT_FAILURE);
    }
    newNode->num_keys = 0;
    newNode->is_leaf = is_leaf;
    for (int i = 0; i < M; i++) {
        newNode->children[i] = NULL;
    }
    return newNode;
}

// Função de divisão de um nó (split)
// Divide um nó que está cheio, criando um novo nó à direita e movendo metade
// das chaves do nó cheio para o novo nó. Também ajusta os ponteiros do nó pai.
void splitChild(struct BTreeNode *parent, int index) {
    struct BTreeNode *child = parent->children[index];
    struct BTreeNode *newNode = createNode(child->is_leaf);
    
    newNode->num_keys = M/2 - 1;

    // Copia metade das chaves e ponteiros do nó filho para o novo nó
    for (int i = 0; i < M/2 - 1; i++) {
        strcpy(newNode->keys[i], child->keys[i + M/2]);
        newNode->rnn[i] = child->rnn[i + M/2];
    }

    if (!child->is_leaf) {
        for (int i = 0; i < M/2; i++) {
            newNode->children[i] = child->children[i + M/2];
        }
    }

    child->num_keys = M/2 - 1;

    // Ajusta o nó pai para inserir o novo nó criado
    for (int i = parent->num_keys; i > index; i--) {
        parent->children[i + 1] = parent->children[i];
    }

    parent->children[index + 1] = newNode;

    for (int i = parent->num_keys - 1; i >= index; i--) {
        strcpy(parent->keys[i + 1], parent->keys[i]);
        parent->rnn[i + 1] = parent->rnn[i];
    }

    strcpy(parent->keys[index], child->keys[M/2 - 1]);
    parent->rnn[index] = child->rnn[M/2 - 1];
    parent->num_keys++;
}

// Função de inserção para um nó não cheio
// Realiza a inserção de uma nova chave em um nó que ainda possui espaço.
void insertNonFull(struct BTreeNode *node, char *key) {
    int i = node->num_keys - 1;
    
    if (node->is_leaf) {
        // Insere a chave e o RNN em ordem crescente
        while (i >= 0 && (strcmp(node->keys[i], key) > 0)) {
            strcpy(node->keys[i + 1], node->keys[i]);
            node->rnn[i + 1] = node->rnn[i];
            i--;
        }
        strcpy(node->keys[i + 1], key);
        node->rnn[i + 1] = current_rnn++;  // Define o RNN automaticamente e incrementa
        node->num_keys++;
    } else {
        // Encontra o filho para inserir a chave
        while (i >= 0 && strcmp(node->keys[i], key) > 0) {
            i--;
        }
        i++;
        
        if (node->children[i]->num_keys == M - 1) {
            // Divide o filho se ele estiver cheio
            splitChild(node, i);
            
            // Determina qual dos dois filhos é o novo para a inserção
            if (strcmp(node->keys[i], key) < 0) {
                i++;
            }
        }
        insertNonFull(node->children[i], key);
    }
}

// Função principal de inserção na árvore B
// Verifica se a raiz está cheia antes de realizar a inserção. Se estiver,
// cria uma nova raiz e divide a antiga.
void insert(struct BTreeNode **root, char *key) {
    struct BTreeNode *node = *root;

    if (node == NULL) {
        // Cria um novo nó raiz
        *root = createNode(true);
        strcpy((*root)->keys[0], key);
        (*root)->rnn[0] = current_rnn++;  // Define o RNN automaticamente e incrementa
        (*root)->num_keys = 1;
    } else {
        if (node->num_keys == M - 1) {
            // Divide a raiz se ela estiver cheia
            struct BTreeNode *new_root = createNode(false);
            new_root->children[0] = node;
            splitChild(new_root, 0);
            *root = new_root;
        }
        insertNonFull(*root, key);
    }
}

// Função para buscar uma chave na Árvore B
int search(struct BTreeNode *node, char *key) {
    int i = 0;

    // Encontra a primeira chave maior ou igual a `key`
    while (i < node->num_keys && strcmp(key, node->keys[i]) > 0) {
        i++;
    }

    // Verifica se a chave foi encontrada
    if (i < node->num_keys && strcmp(node->keys[i], key) == 0) {
        return node->rnn[i]; // Retorna o RNN correspondente
    }

    // Se o nó é folha e a chave não foi encontrada
    if (node->is_leaf) {
        return -1; // Indica que a chave não está na árvore
    }

    // Recorre ao filho apropriado
    return search(node->children[i], key);
}

// Função para encontrar o maior valor de RNN
// Percorre todos os nós da árvore B e retorna o maior RNN encontrado.
int findMaxRNN(struct BTreeNode *root) {
    if (root == NULL) return -1;

    int max_rnn = -1;
    for (int i = 0; i < root->num_keys; i++) {
        if (root->rnn[i] > max_rnn) {
            max_rnn = root->rnn[i];
        }
    }

    for (int i = 0; i <= root->num_keys; i++) {
        if (root->children[i] != NULL) {
            int child_max = findMaxRNN(root->children[i]);
            if (child_max > max_rnn) {
                max_rnn = child_max;
            }
        }
    }

    return max_rnn;
}

// Função para salvar a árvore B em um arquivo
// Grava todos os nós da árvore B em um arquivo binário.
void saveTreeToFile(struct BTreeNode *root, FILE *file) {
    if (root == NULL) return;

    fwrite(root, sizeof(struct BTreeNode), 1, file);

    for (int i = 0; i <= root->num_keys; i++) {
        if (root->children[i] != NULL) {
            saveTreeToFile(root->children[i], file);
        }
    }
}

// Função para ler e inserir placas na árvore B
// Lê registros do arquivo "veiculos.dat" e insere suas placas na árvore B.
void lerEInserirPlacas(struct BTreeNode **root) {
    FILE *datFile = fopen("veiculos.dat", "rb");
    if (datFile == NULL) {
        perror("Erro ao abrir o arquivo veiculos.dat");
        return;
    }

    Veiculo veiculo;
    size_t registros_lidos;
    while ((registros_lidos = fread(&veiculo, TAMANHO_REGISTRO, 1, datFile)) == 1) {
        insert(root, veiculo.placa);
    }

    if (ferror(datFile)) {
        perror("Erro ao ler o arquivo veiculos.dat");
    }

    fclose(datFile);
    printf("Todas as placas foram lidas e inseridas na árvore B.\n");
}

// Função para carregar a árvore B de um arquivo
// Lê a árvore B a partir de um arquivo binário e recria a estrutura na memória.
struct BTreeNode *loadTreeFromFile(FILE *file) {
    struct BTreeNode *node = (struct BTreeNode *)malloc(sizeof(struct BTreeNode));
    if (node == NULL) {
        perror("Falha na alocação de memória");
        exit(EXIT_FAILURE);
    }
    
    if (fread(node, sizeof(struct BTreeNode), 1, file) != 1) {
        free(node);
        return NULL;
    }

    for (int i = 0; i <= node->num_keys; i++) {
        if (!node->is_leaf) {
            node->children[i] = loadTreeFromFile(file);
        } else {
            node->children[i] = NULL;
        }
    }

    return node;
}

// Função de inicialização ou carregamento da árvore B
// Inicializa a árvore B de um arquivo ou insere placas se o arquivo não existir.
struct BTreeNode *initializeOrLoadTree(char *filename) {
    FILE *file = fopen(filename, "rb");

    struct BTreeNode *root;

    if (file == NULL) {
        // Arquivo não existe, cria um novo arquivo e salva a árvore inicial vazia
        file = fopen(filename, "wb+");
        if (file == NULL) {
            perror("Falha ao criar o arquivo");
            exit(EXIT_FAILURE);
        }
        root = NULL;  // Define árvore vazia
        lerEInserirPlacas(&root);
        printf("Arquivo criado: %s\n", filename);
    } else {
        // Carrega a árvore do arquivo existente
        root = loadTreeFromFile(file);
        fclose(file);
        printf("Árvore carregada do arquivo: %s\n", filename);

        // Encontra o maior RNN para ajustar `current_rnn`
        int max_rnn = findMaxRNN(root);
        current_rnn = max_rnn + 1; // Define `current_rnn` para o próximo valor
    }
    return root;
}

// Função para salvar um veículo em "veiculos.dat"
// Abre o arquivo de veículos e adiciona um novo registro ao final.
void salvarVeiculoNoArquivo(Veiculo *veiculo) {
    FILE *datFile = fopen("veiculos.dat", "ab");
    if (datFile == NULL) {
        perror("Erro ao abrir o arquivo veiculos.dat para escrita");
        return;
    }

    fwrite(veiculo, TAMANHO_REGISTRO, 1, datFile);
    fclose(datFile);
}

// Função para remover uma chave da árvore B
// Remove uma chave (key) da árvore B a partir do nó especificado.
// Caso a chave não esteja em um nó folha, utiliza substituições de predecessor/sucessor ou merge para manter as propriedades da árvore B.
void removeFromBTree(struct BTreeNode *node, char *key) {
    int index = 0;

    // Encontra o índice da chave ou o ponto onde ela deveria estar
    while (index < node->num_keys && strcmp(node->keys[index], key) < 0)
        index++;

    // Se a chave foi encontrada no nó atual
    if (index < node->num_keys && strcmp(node->keys[index], key) == 0) {
        if (node->is_leaf)
            removeFromLeaf(node, index);  // Remover do nó folha
        else
            removeFromNonLeaf(node, index);  // Remover de um nó não folha
    } else {
        // Caso a chave não esteja no nó atual e o nó seja folha
        if (node->is_leaf) {
            printf("A chave %s não existe na árvore.\n", key);
            return;
        }

        bool flag = (index == node->num_keys);

        // Se o nó filho onde a chave pode estar tem menos de M/2 chaves, corrige a subárvore
        if (node->children[index]->num_keys < M / 2)
            fill(node, index);

        // Realiza a remoção recursiva no filho apropriado
        if (flag && index > node->num_keys)
            removeFromBTree(node->children[index - 1], key);
        else
            removeFromBTree(node->children[index], key);
    }
}

// Função para remover uma chave de um nó folha
// Remove uma chave localizada no índice especificado de um nó folha.
void removeFromLeaf(struct BTreeNode *node, int index) {
    // Desloca todas as chaves e RNNs uma posição para trás, sobrescrevendo a chave removida
    for (int i = index + 1; i < node->num_keys; ++i) {
        strcpy(node->keys[i - 1], node->keys[i]);
        node->rnn[i - 1] = node->rnn[i];
    }
    node->num_keys--;  // Decrementa o número de chaves do nó
}

// Função para remover uma chave de um nó não folha
// Remove uma chave de um nó não folha, ajustando o nó conforme necessário para manter as propriedades da árvore B.
void removeFromNonLeaf(struct BTreeNode *node, int index) {
    char *key = node->keys[index];

    // Se o filho à esquerda da chave tem pelo menos M/2 chaves
    if (node->children[index]->num_keys >= M / 2) {
        char *pred = getPredecessor(node, index);  // Obtém o predecessor
        strcpy(node->keys[index], pred);
        removeFromBTree(node->children[index], pred);  // Remove o predecessor
    }
    // Se o filho à direita da chave tem pelo menos M/2 chaves
    else if (node->children[index + 1]->num_keys >= M / 2) {
        char *succ = getSuccessor(node, index);  // Obtém o sucessor
        strcpy(node->keys[index], succ);
        removeFromBTree(node->children[index + 1], succ);  // Remove o sucessor
    }
    // Caso contrário, faz o merge dos filhos esquerdo e direito
    else {
        merge(node, index);
        removeFromBTree(node->children[index], key);
    }
}

// Função para obter o predecessor de uma chave
// Encontra o maior valor no filho esquerdo da chave em node->keys[index]
char* getPredecessor(struct BTreeNode *node, int index) {
    struct BTreeNode *current = node->children[index];
    while (!current->is_leaf)
        current = current->children[current->num_keys];  // Percorre até o nó folha mais à direita
    return current->keys[current->num_keys - 1];
}

// Função para obter o sucessor de uma chave
// Encontra o menor valor no filho direito da chave em node->keys[index]
char* getSuccessor(struct BTreeNode *node, int index) {
    struct BTreeNode *current = node->children[index + 1];
    while (!current->is_leaf)
        current = current->children[0];  // Percorre até o nó folha mais à esquerda
    return current->keys[0];
}

// Função para preencher o filho que tem menos de M/2 chaves
// Garante que o filho no índice especificado tenha pelo menos M/2 chaves, usando técnicas de "empréstimo" ou "merge".
void fill(struct BTreeNode *node, int index) {
    if (index != 0 && node->children[index - 1]->num_keys >= M / 2)
        borrowFromPrev(node, index);  // Empresta do irmão à esquerda
    else if (index != node->num_keys && node->children[index + 1]->num_keys >= M / 2)
        borrowFromNext(node, index);  // Empresta do irmão à direita
    else {
        // Faz o merge do filho com o irmão
        if (index != node->num_keys)
            merge(node, index);
        else
            merge(node, index - 1);
    }
}

// Função para emprestar uma chave do nó irmão esquerdo
// Move uma chave do nó pai para o nó filho e uma chave do irmão esquerdo para o pai.
void borrowFromPrev(struct BTreeNode *node, int index) {
    struct BTreeNode *child = node->children[index];
    struct BTreeNode *sibling = node->children[index - 1];

    // Move todas as chaves e ponteiros do filho uma posição à direita
    for (int i = child->num_keys - 1; i >= 0; --i) {
        strcpy(child->keys[i + 1], child->keys[i]);
        child->rnn[i + 1] = child->rnn[i];
    }

    if (!child->is_leaf) {
        for (int i = child->num_keys; i >= 0; --i)
            child->children[i + 1] = child->children[i];
    }

    // Insere a chave do pai no início do filho
    strcpy(child->keys[0], node->keys[index - 1]);
    child->rnn[0] = node->rnn[index - 1];

    if (!node->is_leaf)
        child->children[0] = sibling->children[sibling->num_keys];

    // Move a última chave do irmão para o pai
    strcpy(node->keys[index - 1], sibling->keys[sibling->num_keys - 1]);
    node->rnn[index - 1] = sibling->rnn[sibling->num_keys - 1];

    child->num_keys += 1;
    sibling->num_keys -= 1;
}

// Função para emprestar uma chave do nó irmão direito
// Move uma chave do pai para o filho e uma chave do irmão direito para o pai.
void borrowFromNext(struct BTreeNode *node, int index) {
    struct BTreeNode *child = node->children[index];
    struct BTreeNode *sibling = node->children[index + 1];

    // Move a chave do pai para o final do filho
    strcpy(child->keys[child->num_keys], node->keys[index]);
    child->rnn[child->num_keys] = node->rnn[index];

    if (!(child->is_leaf))
        child->children[child->num_keys + 1] = sibling->children[0];

    // Move a primeira chave do irmão para o pai
    strcpy(node->keys[index], sibling->keys[0]);
    node->rnn[index] = sibling->rnn[0];

    for (int i = 1; i < sibling->num_keys; ++i) {
        strcpy(sibling->keys[i - 1], sibling->keys[i]);
        sibling->rnn[i - 1] = sibling->rnn[i];
    }

    if (!sibling->is_leaf) {
        for (int i = 1; i <= sibling->num_keys; ++i)
            sibling->children[i - 1] = sibling->children[i];
    }

    child->num_keys += 1;
    sibling->num_keys -= 1;
}

// Função de merge (união) entre dois filhos
// Mescla dois nós filhos e uma chave do nó pai, formando um único nó.
void merge(struct BTreeNode *node, int index) {
    struct BTreeNode *child = node->children[index];
    struct BTreeNode *sibling = node->children[index + 1];

    // Insere a chave do nó pai entre o filho e o irmão
    strcpy(child->keys[M / 2 - 1], node->keys[index]);
    child->rnn[M / 2 - 1] = node->rnn[index];

    // Copia as chaves e filhos do irmão para o filho
    for (int i = 0; i < sibling->num_keys; ++i) {
        strcpy(child->keys[i + M / 2], sibling->keys[i]);
        child->rnn[i + M / 2] = sibling->rnn[i];
    }

    if (!child->is_leaf) {
        for (int i = 0; i <= sibling->num_keys; ++i)
            child->children[i + M / 2] = sibling->children[i];
    }

    // Ajusta as chaves e filhos do nó pai
    for (int i = index + 1; i < node->num_keys; ++i) {
        strcpy(node->keys[i - 1], node->keys[i]);
        node->rnn[i - 1] = node->rnn[i];
    }

    for (int i = index + 2; i <= node->num_keys; ++i)
        node->children[i - 1] = node->children[i];

    child->num_keys += sibling->num_keys + 1;
    node->num_keys--;

    free(sibling);  // Libera o nó irmão
}

// Função para remover um veículo do arquivo "veiculos.dat"
// Localiza o veículo pelo RNN e remove o registro do arquivo, reorganizando-o.
void removeVeiculoDoArquivo(int rnn) {
    FILE *datFile = fopen("veiculos.dat", "rb+");
    if (datFile == NULL) {
        perror("Erro ao abrir o arquivo veiculos.dat para remoção");
        return;
    }

    fseek(datFile, 0, SEEK_END);
    long tamanhoArquivo = ftell(datFile);
    int totalRegistros = tamanhoArquivo / TAMANHO_REGISTRO;

    if (rnn == totalRegistros - 1) {
        ftruncate(fileno(datFile), (totalRegistros - 1) * TAMANHO_REGISTRO);
    } else {
        Veiculo ultimoVeiculo;
        fseek(datFile, (totalRegistros - 1) * TAMANHO_REGISTRO, SEEK_SET);
        fread(&ultimoVeiculo, TAMANHO_REGISTRO, 1, datFile);

        fseek(datFile, rnn * TAMANHO_REGISTRO, SEEK_SET);
        fwrite(&ultimoVeiculo, TAMANHO_REGISTRO, 1, datFile);

        ftruncate(fileno(datFile), (totalRegistros - 1) * TAMANHO_REGISTRO);
    }

    fclose(datFile);
    printf("Veículo removido do arquivo com sucesso.\n");
}

// Função principal
// Menu interativo que permite ao usuário buscar, adicionar e remover veículos
int main() {
    char *filename = "b_treeM.idx";
    struct BTreeNode *root = initializeOrLoadTree(filename);
    FilaPagina fila;
    Veiculo veiculo;
    int option;
    char placa[TAMANHO_PLACA];

    inicializarFila(&fila);
    adicionarPagina(&fila, root->keys[0], root->rnn[0]);

    do {
        printf("\nMENU:\n");
        printf("1. Buscar Veículo (pela placa)\n");
        printf("2. Adicionar Veículo\n");
        printf("3. Remover Veículo\n");
        printf("4. Sair\n");
        printf("Escolha uma opção: "); 
        scanf("%d", &option);

        switch (option) {
            case 1: {
                FILE *datFile = fopen("veiculos.dat", "rb");
                if (datFile == NULL) {
                    perror("Erro ao abrir o arquivo veiculos.dat");
                    break;
                }

                printf("Digite a placa do veículo: ");
                scanf("%s", placa);

                int rnn = buscarPagina(&fila, placa);

                if (rnn == -1) {
                    rnn = search(root, placa);

                    if (rnn != -1) {
                        if (fseek(datFile, rnn * TAMANHO_REGISTRO, SEEK_SET) != 0) {
                            perror("Erro ao buscar o registro");
                            fclose(datFile);
                            break;
                        }

                        size_t registros_lidos = fread(&veiculo, TAMANHO_REGISTRO, 1, datFile);
                        if (registros_lidos != 1) {
                            printf("Erro ao ler o registro ou registro inexistente.\n");
                            fclose(datFile);
                            break;
                        }

                        printf("Registro de RNN %d\n", rnn);
                        printf("Placa: %s\n", veiculo.placa);
                        printf("Modelo: %s\n", veiculo.modelo);
                        printf("Marca: %s\n", veiculo.marca);
                        printf("Ano: %d\n", veiculo.ano);
                        printf("Categoria: %s\n", veiculo.categoria);
                        printf("Quilometragem: %d\n", veiculo.quilometragem);
                        printf("Status: %s\n", veiculo.status);
                        printf("---------------------------\n");

                        adicionarPagina(&fila, veiculo.placa, rnn);
                    } else {
                        printf("Veículo não encontrado.\n");
                    }
                } else {
                    if (fseek(datFile, rnn * TAMANHO_REGISTRO, SEEK_SET) != 0) {
                        perror("Erro ao buscar o registro");
                        fclose(datFile);
                        break;
                    }

                    size_t registros_lidos = fread(&veiculo, TAMANHO_REGISTRO, 1, datFile);
                    if (registros_lidos != 1) {
                        printf("Erro ao ler o registro ou registro inexistente.\n");
                        fclose(datFile);
                        break;
                    }

                    printf("Registro de RNN %d\n", rnn);
                    printf("Placa: %s\n", veiculo.placa);
                    printf("Modelo: %s\n", veiculo.modelo);
                    printf("Marca: %s\n", veiculo.marca);
                    printf("Ano: %d\n", veiculo.ano);
                    printf("Categoria: %s\n", veiculo.categoria);
                    printf("Quilometragem: %d\n", veiculo.quilometragem);
                    printf("Status: %s\n", veiculo.status);
                    printf("---------------------------\n");
                }
                fclose(datFile);
                break;
            }
            
            case 2: {
                printf("Digite a placa do veículo: ");
                scanf("%s", veiculo.placa);

                printf("Digite o modelo do veículo: ");
                scanf("%s", veiculo.modelo);

                printf("Digite a marca do veículo: ");
                scanf("%s", veiculo.marca);

                printf("Digite o ano do veículo: ");
                scanf("%d", &veiculo.ano);

                printf("Digite a categoria do veículo: ");
                scanf("%s", veiculo.categoria);

                printf("Digite a quilometragem do veículo: ");
                scanf("%d", &veiculo.quilometragem);

                printf("Digite o status do veículo: ");
                scanf("%s", veiculo.status);

                salvarVeiculoNoArquivo(&veiculo);
                insert(&root, veiculo.placa);

                printf("Veículo adicionado com sucesso.\n");
                break;
            }
            
            case 3: {
                printf("Digite a placa do veículo a ser removido: ");
                scanf("%s", placa);

                int rnn = search(root, placa);
                if (rnn != -1) {
                    removeVeiculoDoArquivo(rnn);
                    removeFromBTree(root, placa);
                    removerPagina(&fila, placa);

                    printf("Veículo removido com sucesso.\n");
                } else {
                    printf("Veículo não encontrado na árvore.\n");
                }
                break;
            }
            
            case 4:
                printf("Saindo...\n");
                break;

            default:
                printf("Opção inválida! Tente novamente.\n");
        }
    } while (option != 4);

    FILE *file = fopen(filename, "wb");
    if (file != NULL) {
        saveTreeToFile(root, file);
        fclose(file);
        printf("Árvore salva no arquivo: %s\n", filename);
    }

    return 0;
}
