import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import models.Client;
import models.Product;
import reporter.CSVReporter;
import reporter.JSONReporter;
import reporter.ReporterProcessorOCP;

import java.util.List;
import java.util.Scanner;

void main() {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
    EntityManager em = emf.createEntityManager();
    Scanner scanner = new Scanner(System.in);
    ReporterProcessorOCP reporterProcessor = new ReporterProcessorOCP();

    boolean executando = true;

    try {
        while (executando) {
            IO.println("\n==================================");
            IO.println("        SISTEMA DE GESTÃO         ");
            IO.println("==================================");
            IO.println("1. Listar todos os Clientes");
            IO.println("2. Listar todos os Produtos");
            IO.println("3. Cadastrar novo Cliente");
            IO.println("4. Cadastrar novo Produto");
            IO.println("5. Exportar Clientes (JSON/CSV)");
            IO.println("6. Exportar Produtos (JSON/CSV)");
            IO.println("0. Sair");
            IO.print("Escolha uma opção: ");

            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1:
                    List<Client> clients = em.createQuery("SELECT c FROM Client c", Client.class).getResultList();
                    IO.println("\n--- Clientes Cadastrados ---");
                    for (Client c : clients) {
                        IO.println(" ID: " + c.getId() + " | Nome: " + c.getName() + " | Email: " + c.getEmail());
                    }
                    break;

                case 2:
                    List<Product> products = em.createQuery("SELECT p FROM Product p", Product.class).getResultList();
                    IO.println("\n--- Produtos Cadastrados ---");
                    for (Product p : products) {
                        IO.println(" ID: " + p.getId() + " | Descrição: " + p.getDescription() + " | Preço: R$" + p.getPrice());
                    }
                    break;

                case 3:
                    IO.print("Digite o nome do cliente: ");
                    String nome = scanner.nextLine();
                    IO.print("Digite o email do cliente: ");
                    String email = scanner.nextLine();

                    em.getTransaction().begin();
                    Client novoCliente = new Client(nome, email);
                    em.persist(novoCliente);
                    em.getTransaction().commit();

                    IO.println("Cliente cadastrado com sucesso!");
                    break;

                case 4:
                    IO.print("Digite a descrição do produto: ");
                    String descricao = scanner.nextLine();
                    IO.print("Digite o preço do produto (ex: 1500.50): ");
                    double preco = Double.parseDouble(scanner.nextLine());

                    em.getTransaction().begin();
                    Product novoProduto = new Product(descricao, preco);
                    em.persist(novoProduto);
                    em.getTransaction().commit();

                    IO.println("Produto cadastrado com sucesso!");
                    break;

                case 5:
                    List<Client> exportedClients = em.createQuery("SELECT c FROM Client c", Client.class).getResultList();

                    IO.println("Escolha o formato: 1 para JSON | 2 para CSV");
                    int formato = Integer.parseInt(scanner.nextLine());

                    if (formato == 1) {
                        reporterProcessor.process(new JSONReporter<>(exportedClients, "clientes_export.json"));
                    } else if (formato == 2) {
                        reporterProcessor.process(new CSVReporter<>(
                                exportedClients,
                                "clientes_export.csv",
                                new String[]{"ID", "Name", "Email"},
                                c -> new Object[]{c.getId(), c.getName(), c.getEmail()}
                        ));
                    } else {
                        IO.println("Formato inválido.");
                    }
                    break;

                case 6:
                    List<Product> exportedProducts = em.createQuery("SELECT p FROM Product p", Product.class).getResultList();

                    IO.println("Escolha o formato: 1 para JSON | 2 para CSV");
                    int formatoProdutos = Integer.parseInt(scanner.nextLine());

                    if (formatoProdutos == 1) {
                        reporterProcessor.process(new JSONReporter<>(exportedProducts, "produtos_export.json"));
                    } else if (formatoProdutos == 2) {
                        reporterProcessor.process(new CSVReporter<>(
                                exportedProducts,
                                "produtos_export.csv",
                                new String[]{"ID", "Description", "Price"},
                                p -> new Object[]{p.getId(), p.getDescription(), p.getPrice()}
                        ));
                    } else {
                        IO.println("Formato inválido.");
                    }
                    break;

                case 0:
                    executando = false;
                    IO.println("Encerrando o sistema...");
                    break;

                default:
                    IO.println("Opção inválida! Tente novamente.");
            }
        }
    } catch (NumberFormatException e) {
        System.err.println("Erro: Você digitou um valor inválido onde era esperado um número. Reinicie o sistema.");
    } catch (Exception e) {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        System.err.println("Erro inesperado no sistema: " + e.getMessage());
        e.printStackTrace();
    } finally {
        scanner.close();
        em.close();
        emf.close();
    }
}