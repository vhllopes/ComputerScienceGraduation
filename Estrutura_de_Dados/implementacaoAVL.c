//VITOR HUGO LELLIS LOPES
#include <stdio.h>
#include <stdlib.h>

// Definição da estrutura do nó da AVL
typedef struct No {
    int valor;
    struct No *esquerda;
    struct No *direita;
    int altura;
} No;

//gera um novo Nó
No* novoNo(int valor){
    No* novo = malloc(sizeof(No));

    if(novo){
        novo->valor = valor;
        novo->esquerda = NULL;
        novo->direita = NULL;
        novo->altura = 0;
    }
    else{
        printf("ERRO AO ALOCAR MEMORIA...");
    }
}

//retorna maior altura comparada entre dois nós
int maior(int a, int b){
    if(a>b){
        return a;
    }
    else{
        return b;
    }
}

//atura de um nó
int altura(No *no){
    if(no == NULL){
        return -1;
    }
    else{
        return no->altura;
    }
}

//Calcula o fator de balanceamento de um nó
int fatorDeBalanceamento(No *no){
    if(no){
        return (altura(no->esquerda) - altura(no->direita));
    }
    else{
        return 0;
    }
}

No* rotacaoEsquerda(No *r){
    No *y, *f;

    y = r->direita;
    f = y->esquerda;

    y->esquerda = r;
    r->direita = f;

    r->altura = maior(altura(r->esquerda), altura(r->direita)) + 1;
    y->altura = maior(altura(y->esquerda), altura(y->direita)) + 1;

    return y;
}

No* rotacaoDireita(No *r){
    No *y, *f;

    y = r->esquerda;
    f = y->direita;

    y->direita = r;
    r->esquerda = f;

    r->altura = maior(altura(r->esquerda), altura(r->direita)) + 1;
    y->altura = maior(altura(y->esquerda), altura(y->direita)) + 1;

    return y;
}

No* rotacaoDireitaEsquerda(No * r){
    r->direita = rotacaoDireita(r->direita);
    return rotacaoEsquerda(r);
}

No* rotacaoEsquerdaDireita(No *r){
    r->esquerda = rotacaoEsquerda(r->esquerda);
    return rotacaoDireita(r);
}

//Função que realiza o balanceamento de uma arvore AVL
No* balancear(No *raiz){
    int fb = fatorDeBalanceamento(raiz);

    if(fb < -1 && fatorDeBalanceamento(raiz->direita) <= 0){
        raiz = rotacaoEsquerda(raiz);
    }

    else if(fb > 1 && fatorDeBalanceamento(raiz->esquerda) >= 0){
        raiz = rotacaoDireita(raiz);
    }

    else if(fb > 1 && fatorDeBalanceamento(raiz->esquerda) < 0){
        raiz = rotacaoEsquerdaDireita(raiz);
    }
    else if(fb < -1 && fatorDeBalanceamento(raiz->direita) > 0){
        raiz = rotacaoDireitaEsquerda(raiz);
    }

    return raiz;
}

//Inserção
No* inserir(No *raiz, int x){
    if(raiz == NULL) //arvore vazia
        return novoNo(x);
    else{
        if(x<raiz->valor){
            raiz->esquerda = inserir(raiz->esquerda, x);
        }
        else if(x > raiz->valor){
            raiz->direita = inserir(raiz->direita, x);
        }
        else{
            printf("\nInsercao nao realizada\nO elemento em questao nao existe...");
        }
    }

    // Recalcula a altura de todos os nós entre a raiz e o novo nó inserido
    raiz->altura = maior(altura(raiz->esquerda), altura(raiz->direita)) + 1;

    // verifica a necessidade de rebalancear a árvore
    raiz = balancear(raiz);

    return raiz;
}

//Remoção
No* remover(No * raiz, int chave){
    if(raiz == NULL){
        printf("Valor nao encontrado!\n");
        return NULL;
    }
    else{
        if(raiz->valor == chave){
            //remoção nós folhas
            if(raiz->direita == NULL && raiz->esquerda == NULL){
                free(raiz);
                printf("Elemento folha removido: %d !\n", chave);
                return NULL;
            }
            else{
                //remoção nós que possuem apenas 1 filho
                if(raiz->esquerda != NULL || raiz->direita != NULL){
                    No *aux;
                    while(aux->direita != NULL){
                        aux = aux->direita;
                    }
                    raiz->valor = aux->valor;
                    aux->valor = chave;
                    printf("Elemento trocado: %d !\n", chave);
                    raiz->esquerda = remover(raiz->esquerda, chave);
                    return raiz;
                }
                //remoção nós com 2 filhos
                else{
                    No *aux;
                    if(raiz->esquerda != NULL){
                        aux = raiz->esquerda;
                    }
                    else{
                        aux = raiz->direita;
                    }
                    free(raiz);
                    printf("Elemento com 1 filho removido: %d !\n", chave);
                    return aux;
                }
            }
        }
        else{
            if(chave < raiz->valor){
                raiz->esquerda = remover(raiz->esquerda, chave);
            }
            else{
                raiz->direita = remover(raiz->direita, chave);
            }
        }
        
        //Recalcula a altura de todos os nós...
        raiz->altura = maior(altura(raiz->esquerda), altura(raiz->direita)) + 1;
        
        //verifica a necessidade de rebalancear a arvore...
        raiz = balancear(raiz);

        return raiz;
    }
}

//Impressão dos elementos de uma AVL
void imprimir(No *raiz, int nivel){
    int i;
    if(raiz){
        imprimir(raiz->direita, nivel + 1);
        printf("\n\n");

        for(i=0; i< nivel; i++){
            printf("\t");
        }
        printf("%d", raiz->valor);
        imprimir(raiz->esquerda, nivel + 1);
    }
}

//***********************(MAIN)**********************
int main(){
    int opcao, valor;
    No * raiz = NULL;

    do{
        printf("\n\n\t0 - Sair\n\t1 - Inserir\n\t2 - Remover\n\t3 - Imprimir\n\n");
        scanf("%d", &opcao);

        switch (opcao)
        {
        case 0:
            printf("Saindo!!!");
            break;
        case 1:
            printf("\tDigite o valor a ser inserido: ");
            scanf("%d", &valor);
            raiz = inserir(raiz, valor);
            break;
        case 2:
            printf("\tDigite o valor a ser removido: ");
            scanf("%d", &valor);
            raiz = remover(raiz, valor);
            break;
        case 3:
            imprimir(raiz, 1);
            break;
        default:
            printf("\nOpcao invalida!!!\n");
        }
    }while(opcao != 0);

    return 0;
}