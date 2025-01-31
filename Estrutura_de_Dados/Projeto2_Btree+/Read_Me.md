# Gerenciamento de Veículos com Árvore B+

Este projeto implementa um sistema de gerenciamento de veículos utilizando **Árvore B+**, uma estrutura eficiente para indexação e recuperação rápida de registros armazenados em um arquivo binário (`veiculos.dat`).

## Funcionalidades

-  **Gerenciamento de Veículos**:

  - Cadastro de novos veículos.
  - Busca otimizada por veículos usando **placas como chaves**.
  - Remoção segura de veículos do banco de dados.
  - Listagem eficiente de veículos dentro de um intervalo de placas.

-  **Uso da Árvore B+**:

  - Implementação de **nós folhas interligados** para buscas rápidas.
  - Estrutura balanceada garantindo alta performance.
  - Persistência da árvore em um arquivo binário (`btree.idx`).

##  Estrutura do Projeto

 **Arquivos Principais**

- `bTreePlus.c` - Código principal da implementação da Árvore B+.
- `veiculos.dat` - Arquivo binário contendo os registros de veículos.
- `btree.idx` - Arquivo binário armazenando a estrutura da Árvore B+.

## Exemplo de Uso

```
--- MENU ---
1. Buscar Veículo (pela placa)
2. Adicionar Veículo
3. Remover Veículo
4. Listar Itens
5. Sair
Escolha uma opção: 1
Digite a placa do veículo: ABC5678

Veículo encontrado:
Placa: ABC5678
Modelo: Corolla
Marca: Toyota
Ano: 2021
Categoria: Sedan
Quilometragem: 20.000 km
Status: Disponível
```

Dúvidas ou sugestões? Abra uma issue no repositório! 

