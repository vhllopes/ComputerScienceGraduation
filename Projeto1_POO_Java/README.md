# Sistema de Gerenciamento de Loja

Este projeto é um sistema simples de gerenciamento de loja, desenvolvido em **Java**, que permite a criação e manipulação de **categorias de produtos**, **produtos físicos e virtuais**, além de funcionalidades como **busca, adição e remoção de produtos**.

## Funcionalidades

-  **Gerenciamento de Categorias**:
  - Criar, editar e remover categorias.
  - Listar todas as categorias disponíveis.

-  **Gerenciamento de Produtos**:
  - Adicionar produtos físicos e virtuais.
  - Buscar produtos por nome ou ID.
  - Remover e editar produtos.

-  **Carrinho e Compras**:
  - Adicionar produtos ao carrinho.
  - Realizar a compra de produtos.
  - Cálculo de frete para produtos físicos.
  - Download de produtos virtuais.

##  Estrutura do Projeto

O projeto segue uma estrutura modular organizada dentro do **pacote `classes`**:

 **classes/**
- `Categoria.java` - Define a estrutura das categorias de produtos.
- `Loja.java` - Representa a loja e gerencia categorias e produtos.
- `Produto.java` - Classe abstrata para representar produtos.
- `ProdutoFisico.java` - Representa produtos físicos, com cálculo de frete.
- `ProdutoVirtual.java` - Representa produtos virtuais, com suporte a download.
- `DisplayLoja.java` - Interface de interação com o usuário.
- `Main.java` - Classe principal que inicializa a aplicação.

## Exemplo de Uso
Ao iniciar o programa, o usuário verá um menu com opções como **buscar produtos, adicionar ao carrinho e realizar compras**. Exemplo de interação:

```
=== Menu ===
1. Buscar Produto
2. Adicionar ao Carrinho
3. Realizar Compra
4. Gerenciar Categoria
5. Gerenciar Produto
0. Sair
Escolha uma opção: 1
Digite o NOME ou ID do produto que deseja buscar: Notebook
Produto encontrado:
ID: 101
Nome: Notebook Gamer
Preço: R$ 4500.00
Marca: ASUS
Categoria: Eletrônicos
Peso: 2.5kg
Dimensões: 38cm x 25cm x 2cm
```

## Notas
- O projeto lê as **categorias e produtos** a partir de arquivos `.txt`, cujos caminhos estão definidos na `Main.java`. Atualize esses caminhos para os seus arquivos locais antes de executar.

---
 Caso tenha dúvidas ou sugestões, sinta-se à vontade para abrir uma issue ou contribuir!

