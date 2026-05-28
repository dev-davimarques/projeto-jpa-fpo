# Sistema de Gestão de Clientes e Produtos com Exportação de Dados

Este projeto é uma aplicação Java interativa (CLI) para gerenciamento de Clientes e Produtos, integrada a um banco de dados relacional e com suporte a múltiplos formatos de exportação de relatórios.

## 📌 Assunto
O sistema foi desenvolvido para demonstrar a aplicação prática de operações de adição e listage de Clientes e Produtos, utilizando persistência de dados com JPA/Hibernate e a implementação de padrões de projeto para geração de relatórios em formatos JSON e CSV.

## 🛠 Tecnologias Utilizadas
* **Java 25**: Utilizando as últimas funcionalidades da linguagem.
* **JPA / Hibernate**: Para mapeamento objeto-relacional e persistência.
* **MySQL**: Banco de dados relacional.
* **Docker**: Utilizado para containerização do banco de dados, facilitando o setup do ambiente.
* **Jackson 3 (tools.jackson)**: Para processamento e geração de arquivos JSON.
* **Apache Commons CSV**: Para geração de arquivos CSV.
* **Maven**: Gestão de dependências e build do projeto.

## 🏗 Arquitetura e Padrões de Projeto
O projeto foi desenhado seguindo princípios de código limpo e extensível:

1.  **Strategy Pattern**: Utilizado na engine de relatórios. A interface `Reporter` permite que diferentes algoritmos de exportação (JSON, CSV) sejam trocados dinamicamente.
2.  **Open-Closed Principle (OCP)**: Através da classe `ReporterProcessorOCP`, o sistema está aberto para novos formatos de relatório (como PDF ou XML) sem a necessidade de modificar o código existente.
3.  **Generics**: As classes de relatório foram implementadas com tipos genéricos (`<T>`), permitindo que a mesma lógica de exportação funcione para `Client`, `Product` ou qualquer outra entidade futura.

## 🚀 Funcionalidades
* **Cadastro de Clientes**: Registro de nome e email com validação de unicidade.
* **Cadastro de Produtos**: Registro de descrição e preço.
* **Listagem em Tempo Real**: Consulta de dados diretamente do banco de dados MySQL.
* **Exportação Polimórfica**:
    * Geração de arquivo `JSON` com indentação (Pretty Print).
    * Geração de arquivo `CSV` com cabeçalhos personalizados.
* **Interface Interativa**: Menu CLI para facilitar a interação do usuário.

## ⚙️ Como Executar

### 1. Subir o Banco de Dados (Docker)
Certifique-se de ter o Docker instalado e execute o comando para subir o container do MySQL:
```bash
docker run --name mysql-container -e MYSQL_ROOT_PASSWORD=suasenha -e MYSQL_DATABASE=default -p 3306:3306 -d mysql:latest