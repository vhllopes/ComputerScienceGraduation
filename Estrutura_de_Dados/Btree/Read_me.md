Gerenciamento de Veículos com Árvore B
Este projeto implementa um sistema de gerenciamento de veículos utilizando Árvore B para indexação eficiente dos dados armazenados em um arquivo binário (veiculos.dat). O objetivo é fornecer operações rápidas de inserção, busca, remoção e listagem.

Funcionalidades
Gerenciamento de Veículos:

Cadastro de novos veículos.
Busca rápida por veículos usando placas como chaves.
Remoção segura de veículos do banco de dados.
Listagem ordenada de veículos dentro de um intervalo de placas.
Uso da Árvore B:

Indexação eficiente para buscas otimizadas.
Operações de split e merge garantindo balanceamento da estrutura.
Persistência da árvore em um arquivo binário (b_treeM.idx).
 Estrutura do Projeto
 Arquivos Principais

veiculos.c - Código principal para gerenciamento de veículos com Árvore B.
veiculos.dat - Arquivo binário contendo os registros de veículos.
b_treeM.idx - Arquivo binário armazenando a estrutura da Árvore B para indexação.

Exemplo de Uso:

--- MENU ---
1. Buscar Veículo (pela placa)
2. Adicionar Veículo
3. Remover Veículo
4. Listar Itens
5. Sair
Escolha uma opção: 1
Digite a placa do veículo: XYZ1234

Veículo encontrado:
Placa: XYZ1234
Modelo: Civic
Marca: Honda
Ano: 2019
Categoria: Sedan
Quilometragem: 30.000 km
Status: Disponível

Dúvidas ou sugestões? Abra uma issue no repositório!
