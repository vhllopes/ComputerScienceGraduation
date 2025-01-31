#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define ORDEM 3
#define TAMANHO_PLACA 8
#define TAMANHO_MODELO 20
#define TAMANHO_MARCA 20
#define TAMANHO_CATEGORIA 15
#define TAMANHO_STATUS 16
#define TAMANHO_REGISTRO 88 // Tamanho do registro em bytes

typedef struct Veiculo {
    char placa[TAMANHO_PLACA];
    char modelo[TAMANHO_MODELO];
    char marca[TAMANHO_MARCA];
    int ano;
    char categoria[TAMANHO_CATEGORIA];
    int quilometragem;
    char status[TAMANHO_STATUS];
} Veiculo;

typedef struct Node {
    char keys[ORDEM - 1][8];
    int rnn[ORDEM - 1];
    struct Node *children[ORDEM];
    int num_keys;
    bool is_leaf;
    struct Node *next;
} Node;

typedef struct BTree {
    Node *root;
} BTree;

int current_rnn = 0;

bool search(Node *node, const char *key);
void salvarVeiculoNoArquivo(Veiculo *veiculo);


Node *createNode(bool is_leaf) {
    Node *node = (Node *)malloc(sizeof(Node));
    node->num_keys = 0;
    node->is_leaf = is_leaf;
    node->next = NULL;
    for (int i = 0; i < ORDEM; i++) {
        node->children[i] = NULL;
    }
    return node;
}

BTree *createBTree() {
    BTree *btree = (BTree *)malloc(sizeof(BTree));
    btree->root = createNode(true);
    return btree;
}

void saveTreeToFile(Node *root, FILE *file) {
    if (root == NULL) return;

    fwrite(root, sizeof(Node), 1, file);

    for (int i = 0; i <= root->num_keys; i++) {
        if (!root->is_leaf) {
            saveTreeToFile(root->children[i], file);
        }
    }
}

Node *loadTreeFromFile(FILE *file) {
    Node *node = (Node *)malloc(sizeof(Node));
    if (fread(node, sizeof(Node), 1, file) != 1) {
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

void insertNonFull(Node *node, const char *key, int rnn);
void splitChild(Node *parent, int i, Node *child);

void insert(BTree *btree, const char *key) {
    Node *root = btree->root;
    if (root->num_keys == ORDEM - 1) {
        Node *newRoot = createNode(false);
        newRoot->children[0] = root;
        splitChild(newRoot, 0, root);
        insertNonFull(newRoot, key, current_rnn++);
        btree->root = newRoot;
    } else {
        insertNonFull(root, key, current_rnn++);
    }
}

void insertNonFull(Node *node, const char *key, int rnn) {
    int i = node->num_keys - 1;

    if (node->is_leaf) {
        while (i >= 0 && strcmp(key, node->keys[i]) < 0) {
            strcpy(node->keys[i + 1], node->keys[i]);
            node->rnn[i + 1] = node->rnn[i];
            i--;
        }

        strcpy(node->keys[i + 1], key);
        node->rnn[i + 1] = rnn;
        node->num_keys++;
    } else {
        while (i >= 0 && strcmp(key, node->keys[i]) < 0) {
            i--;
        }
        i++;

        if (node->children[i]->num_keys == ORDEM - 1) {
            splitChild(node, i, node->children[i]);
            if (strcmp(key, node->keys[i]) > 0) {
                i++;
            }
        }
        insertNonFull(node->children[i], key, rnn);
    }
}

void splitChild(Node *parent, int i, Node *child) {
    int mid = (ORDEM - 1) / 2;
    Node *newChild = createNode(child->is_leaf);
    newChild->num_keys = ORDEM - 1 - mid - 1;

    for (int j = 0; j < newChild->num_keys; j++) {
        strcpy(newChild->keys[j], child->keys[mid + 1 + j]);
        newChild->rnn[j] = child->rnn[mid + 1 + j];
    }

    if (!child->is_leaf) {
        for (int j = 0; j <= newChild->num_keys; j++) {
            newChild->children[j] = child->children[mid + 1 + j];
        }
    }

    child->num_keys = mid;

    for (int j = parent->num_keys; j >= i + 1; j--) {
        parent->children[j + 1] = parent->children[j];
    }
    parent->children[i + 1] = newChild;

    for (int j = parent->num_keys - 1; j >= i; j--) {
        strcpy(parent->keys[j + 1], parent->keys[j]);
        parent->rnn[j + 1] = parent->rnn[j];
    }

    strcpy(parent->keys[i], child->keys[mid]);
    parent->rnn[i] = child->rnn[mid];
    parent->num_keys++;
}

void lerEInserirPlacas(BTree *btree) {
    FILE *datFile = fopen("veiculos.dat", "rb");
    if (datFile == NULL) {
        perror("Erro ao abrir o arquivo veiculos.dat");
        return;
    }

    Veiculo veiculo;
    while (fread(&veiculo, TAMANHO_REGISTRO, 1, datFile) == 1) {
        insert(btree, veiculo.placa);
    }

    fclose(datFile);
}

BTree *initializeOrLoadTree(const char *filename) {
    FILE *file = fopen(filename, "rb");
    BTree *btree = createBTree();

    if (file == NULL) {
        FILE *newFile = fopen(filename, "wb+");
        if (newFile == NULL) {
            perror("Erro ao criar o arquivo btree.idx");
            exit(EXIT_FAILURE);
        }

        lerEInserirPlacas(btree);
        saveTreeToFile(btree->root, newFile);
        fclose(newFile);
    } else {
        btree->root = loadTreeFromFile(file);
        fclose(file);
    }

    return btree;
}

Veiculo buscarVeiculo(Node *node, const char *key) {
    int i = 0;

    while (i < node->num_keys && strcmp(key, node->keys[i]) > 0) {
        i++;
    }

    if (i < node->num_keys && strcmp(key, node->keys[i]) == 0) {
        FILE *datFile = fopen("veiculos.dat", "rb");
        if (datFile == NULL) {
            perror("Erro ao abrir o arquivo veiculos.dat para leitura");
            exit(EXIT_FAILURE);
        }

        Veiculo veiculo;
        fseek(datFile, node->rnn[i] * TAMANHO_REGISTRO, SEEK_SET);
        fread(&veiculo, TAMANHO_REGISTRO, 1, datFile);
        fclose(datFile);

        return veiculo;
    }

    if (node->is_leaf) {
        fprintf(stderr, "Erro: Veiculo com placa %s não encontrado.\n", key);
        exit(EXIT_FAILURE);
    }

    return buscarVeiculo(node->children[i], key);
}

bool search(Node *node, const char *key) {
    int i = 0;
    while (i < node->num_keys && strcmp(key, node->keys[i]) > 0) {
        i++;
    }
    if (i < node->num_keys && strcmp(key, node->keys[i]) == 0) {
        return true;
    }
    if (node->is_leaf) {
        return false;
    }
    return search(node->children[i], key);
}

void salvarVeiculoNoArquivo(Veiculo *veiculo) {
    FILE *datFile = fopen("veiculos.dat", "ab");
    if (datFile == NULL) {
        perror("Erro ao abrir o arquivo veiculos.dat para escrita");
        exit(EXIT_FAILURE);
    }
    fwrite(veiculo, TAMANHO_REGISTRO, 1, datFile);
    fclose(datFile);
}

void inicializarRNN() {
    FILE *datFile = fopen("veiculos.dat", "rb");
    if (datFile == NULL) {
        current_rnn = 0; // Nenhum registro inicial
        return;
    }

    fseek(datFile, 0, SEEK_END);
    long tamanhoArquivo = ftell(datFile);
    fclose(datFile);

    current_rnn = tamanhoArquivo / TAMANHO_REGISTRO;
}

void listarItensNoRange(Node *root, const char *chaveInicio, const char *chaveFim) {
    if (root == NULL) {
        printf("Árvore está vazia.\n");
        return;
    }

    // Verificar se chaveInicio é maior que chaveFim e trocar se necessário
    char chaveInicialCorrigida[TAMANHO_PLACA], chaveFinalCorrigida[TAMANHO_PLACA];
    if (strcmp(chaveInicio, chaveFim) > 0) {
        strcpy(chaveInicialCorrigida, chaveFim);
        strcpy(chaveFinalCorrigida, chaveInicio);
    } else {
        strcpy(chaveInicialCorrigida, chaveInicio);
        strcpy(chaveFinalCorrigida, chaveFim);
    }

    // Localizar a folha contendo a chave inicial corrigida
    Node *current = root;
    while (!current->is_leaf) {
        int i = 0;
        while (i < current->num_keys && strcmp(chaveInicialCorrigida, current->keys[i]) > 0) {
            i++;
        }
        current = current->children[i];
    }

    // Percorrer as folhas e imprimir as chaves no intervalo
    printf("\n--- Listando Veiculos no Intervalo [%s, %s] ---\n", chaveInicialCorrigida, chaveFinalCorrigida);
    while (current != NULL) {
        for (int i = 0; i < current->num_keys; i++) {
            // Debug
            printf("Debug: Chave atual: %s\n", current->keys[i]);

            // Verificar se a chave está dentro do intervalo
            if (strcmp(current->keys[i], chaveInicialCorrigida) >= 0 && strcmp(current->keys[i], chaveFinalCorrigida) <= 0) {
                printf("Placa: %s\n", current->keys[i]);
            }
        }

        // Se a última chave no nó atual ultrapassar chaveFinalCorrigida, podemos parar
        if (current->num_keys > 0 && strcmp(current->keys[current->num_keys - 1], chaveFinalCorrigida) > 0) {
            break;
        }

        // Ir para o próximo nó folha
        current = current->next;
    }
}

// Função para remover um veículo do arquivo "veiculos.dat"
void removeVeiculoDoArquivo(int rnn) {
    FILE* file = fopen("veiculos.dat", "rb+");
    if (file == NULL) {
        perror("Erro ao abrir o arquivo veiculos.dat");
        return;
    }

    fseek(file, 0, SEEK_END);
    long tamanhoArquivo = ftell(file);
    int totalRegistros = tamanhoArquivo / sizeof(Veiculo);

    if (rnn >= totalRegistros) {
        printf("RNN inválido\n");
        fclose(file);
        return;
    }

    if (rnn == totalRegistros - 1) {
        if (ftruncate(fileno(file), (totalRegistros - 1) * sizeof(Veiculo)) != 0) {
            perror("Erro ao truncar o arquivo");
        }
    } else {
        Veiculo ultimoVeiculo;
        fseek(file, (totalRegistros - 1) * sizeof(Veiculo), SEEK_SET);
        fread(&ultimoVeiculo, sizeof(Veiculo), 1, file);
        fseek(file, rnn * sizeof(Veiculo), SEEK_SET);
        fwrite(&ultimoVeiculo, sizeof(Veiculo), 1, file);
        if (ftruncate(fileno(file), (totalRegistros - 1) * sizeof(Veiculo)) != 0) {
            perror("Erro ao truncar o arquivo");
        }
    }

    fclose(file);
    printf("Veiculo removido do arquivo com sucesso.\n");
}

void redistribute(Node *parent, int idx) {
    Node *left = parent->children[idx];
    Node *right = parent->children[idx + 1];

    if (left->num_keys < (ORDEM - 1) / 2) {
        // Mover chave do irmão direito para o nó atual
        strcpy(left->keys[left->num_keys], parent->keys[idx]);
        left->rnn[left->num_keys] = parent->rnn[idx];
        left->num_keys++;

        strcpy(parent->keys[idx], right->keys[0]);
        parent->rnn[idx] = right->rnn[0];

        // Shift no irmão direito
        for (int i = 0; i < right->num_keys - 1; i++) {
            strcpy(right->keys[i], right->keys[i + 1]);
            right->rnn[i] = right->rnn[i + 1];
        }
        right->num_keys--;
    }
}

void merge(Node *parent, int idx) {
    Node *left = parent->children[idx];
    Node *right = parent->children[idx + 1];

    // Copiar chave do pai para o nó da esquerda
    strcpy(left->keys[left->num_keys], parent->keys[idx]);
    left->rnn[left->num_keys] = parent->rnn[idx];
    left->num_keys++;

    // Copiar todas as chaves do nó direito para o nó esquerdo
    for (int i = 0; i < right->num_keys; i++) {
        strcpy(left->keys[left->num_keys], right->keys[i]);
        left->rnn[left->num_keys] = right->rnn[i];
        left->num_keys++;
    }

    // Se não for folha mover os filhos
    if (!right->is_leaf) {
        for (int i = 0; i <= right->num_keys; i++) {
            left->children[left->num_keys + i] = right->children[i];
        }
    }

    // Ajustar o pai
    for (int i = idx; i < parent->num_keys - 1; i++) {
        strcpy(parent->keys[i], parent->keys[i + 1]);
        parent->rnn[i] = parent->rnn[i + 1];
        parent->children[i + 1] = parent->children[i + 2];
    }
    parent->num_keys--;

    free(right);
}

void rebalance(Node *parent, int idx) {
    Node *current = parent->children[idx];

    // Redistribuir com o irmão direito
    if (idx + 1 < parent->num_keys && parent->children[idx + 1]->num_keys > (ORDEM - 1) / 2) {
        redistribute(parent, idx);
    }
    // Redistribuir com o irmão esquerdo
    else if (idx - 1 >= 0 && parent->children[idx - 1]->num_keys > (ORDEM - 1) / 2) {
        redistribute(parent, idx - 1);
    }
    // Mesclar com o irmão direito
    else if (idx + 1 < parent->num_keys) {
        merge(parent, idx);
    }
    // Mesclar com o irmão esquerdo
    else if (idx - 1 >= 0) {
        merge(parent, idx - 1);
    }
}

void removeFromNode(Node *node, const char *key, Node *parent, int idx) {
    int i = 0;

    // Localiza a posição da chave no nó
    while (i < node->num_keys && strcmp(key, node->keys[i]) > 0) {
        i++;
    }

    // Verifica se a chave está presente no nó
    if (i < node->num_keys && strcmp(key, node->keys[i]) == 0) {
        // Caso 1: O nó é uma folha
        if (node->is_leaf) {
            for (int j = i; j < node->num_keys - 1; j++) {
                strcpy(node->keys[j], node->keys[j + 1]);
                node->rnn[j] = node->rnn[j + 1];
            }
            node->num_keys--;

            // Rebalancear se necessário
            if (parent != NULL && node->num_keys < (ORDEM - 1) / 2) {
                rebalance(parent, idx);
            }
        }
    } else {
        if (node->is_leaf) {
            printf("Erro: A chave %s nao esta presente na arvore.\n", key);
        } else {
            removeFromNode(node->children[i], key, node, i);

            // Verifica se o filho precisa de reequilíbrio após a remoção
            if (node->children[i]->num_keys < (ORDEM - 1) / 2) {
                rebalance(node, i);
            }
        }
    }
}


int main() {
    BTree *btree = initializeOrLoadTree("btree.idx");
    Veiculo veiculo;
    char placa[TAMANHO_PLACA];
    int option;
    char placa1[TAMANHO_PLACA], placa2[TAMANHO_PLACA];

    do {
        printf("\n--- MENU ---\n");
        printf("1. Buscar veiculo (pela placa)\n");
        printf("2. Adicionar veiculo\n");
        printf("3. Remover veiculo\n");
        printf("4. Listar Itens\n");
        printf("5. Sair\n");
        printf("Escolha uma opcao: ");
        scanf("%d", &option);

        switch (option) {
            case 1: // Buscar veículo
                printf("Digite a placa do veiculo a ser buscado: ");
                scanf("%s", placa);

                if (search(btree->root, placa)) {
                    printf("Veiculo com placa %s encontrado na arvore B+.\n", placa);

                    Veiculo encontrado = buscarVeiculo(btree->root, placa);
                    printf("Detalhes do veiculo:\n");
                    printf("Placa: %s\n", encontrado.placa);
                    printf("Modelo: %s\n", encontrado.modelo);
                    printf("Marca: %s\n", encontrado.marca);
                    printf("Ano: %d\n", encontrado.ano);
                    printf("Categoria: %s\n", encontrado.categoria);
                    printf("Quilometragem: %d\n", encontrado.quilometragem);
                    printf("Status: %s\n", encontrado.status);
                } else {
                    printf("Veículo com placa %s não encontrado.\n", placa);
                }
                break;
            case 2: // Adicionar veículo
                printf("Digite os detalhes do veiculo:\n");
                printf("Placa: ");
                scanf("%s", veiculo.placa);
                printf("Modelo: ");
                scanf("%s", veiculo.modelo);
                printf("Marca: ");
                scanf("%s", veiculo.marca);
                printf("Ano: ");
                scanf("%d", &veiculo.ano);
                printf("Categoria: ");
                scanf("%s", veiculo.categoria);
                printf("Quilometragem: ");
                scanf("%d", &veiculo.quilometragem);
                printf("Status: ");
                scanf("%s", veiculo.status);

                salvarVeiculoNoArquivo(&veiculo);
                insert(btree, veiculo.placa);

                printf("Veiculo adicionado com sucesso.\n");
                break;

            case 3: // Remover veículo
                printf("Digite a placa do veiculo a ser removido: ");
                scanf("%s", placa);

                if (search(btree->root, placa)) {
                    // Localizar o RNN do veículo a ser removido
                    Node *current = btree->root;
                    int rnn = -1;

                    while (current != NULL) {
                        for (int i = 0; i < current->num_keys; i++) {
                            if (strcmp(current->keys[i], placa) == 0) {
                                rnn = current->rnn[i];
                                break;
                            }
                        }

                        if (rnn != -1 || current->is_leaf) {
                            break;
                        }

                        // Continuar buscando nos filhos
                        int i = 0;
                        while (i < current->num_keys && strcmp(placa, current->keys[i]) > 0) {
                            i++;
                        }
                        current = current->children[i];
                    }

                    if (rnn != -1) {
                        removeVeiculoDoArquivo(rnn); // Remove do arquivo
                        removeFromNode(btree->root, placa, NULL, 0); // Remove da árvore
                        printf("Veiculo com placa %s removido com sucesso.\n", placa);
                    } else {
                        printf("Erro: Nao foi possível localizar o RNN do veiculo.\n");
                    }
                } else {
                    printf("Veiculo com placa %s nao encontrado na arvore B+.\n", placa);
                }
                break;

            case 4: // Listar as placas dos veículos
                printf("Insira a primeira placa: ");
                scanf("%s", placa1);
                printf("\nInsira a segunda placa: ");
                scanf("%s", placa2);
                listarItensNoRange(btree->root, placa1, placa2);
                break;

            case 5: // Sair
                printf("Saindo...\n");
                break;

            default:
                printf("Opcao invalida. Tente novamente.\n");
        }
    } while (option != 5);

    // Salvar a árvore B+ no arquivo
    FILE *saveFile = fopen("btree.idx", "wb");
    if (saveFile != NULL) {
        saveTreeToFile(btree->root, saveFile);
        fclose(saveFile);
        printf("Arvore B+ salva no arquivo.\n");
    } else {
        perror("Erro ao salvar a arvore B+ no arquivo");
    }

    return 0;
}
