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
            System.out.println("\n==================================");
            System.out.println("        SISTEMA DE GESTÃO         ");
            System.out.println("==================================");
            System.out.println("1. Listar todos os Clientes");
            System.out.println("2. Listar todos os Produtos");
            System.out.println("3. Cadastrar novo Cliente");
            System.out.println("4. Cadastrar novo Produto");
            System.out.println("5. Exportar Clientes (JSON/CSV)");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1:
                    List<Client> clients = em.createQuery("SELECT c FROM Client c", Client.class).getResultList();
                    System.out.println("\n--- Clientes Cadastrados ---");
                    for (Client c : clients) {
                        System.out.println(" ID: " + c.getId() + " | Nome: " + c.getName() + " | Email: " + c.getEmail());
                    }
                    break;

                case 2:
                    List<Product> products = em.createQuery("SELECT p FROM Product p", Product.class).getResultList();
                    System.out.println("\n--- Produtos Cadastrados ---");
                    for (Product p : products) {
                        System.out.println(" ID: " + p.getId() + " | Descrição: " + p.getDescription() + " | Preço: R$" + p.getPrice());
                    }
                    break;

                case 3:
                    System.out.print("Digite o nome do cliente: ");
                    String nome = scanner.nextLine();
                    System.out.print("Digite o email do cliente: ");
                    String email = scanner.nextLine();

                    em.getTransaction().begin();
                    Client novoCliente = new Client(nome, email);
                    em.persist(novoCliente);
                    em.getTransaction().commit();

                    System.out.println("✅ Cliente cadastrado com sucesso!");
                    break;

                case 4:
                    System.out.print("Digite a descrição do produto: ");
                    String descricao = scanner.nextLine();
                    System.out.print("Digite o preço do produto (ex: 1500.50): ");
                    // Captura a string e converte para double
                    double preco = Double.parseDouble(scanner.nextLine());

                    em.getTransaction().begin();
                    Product novoProduto = new Product(descricao, preco);
                    em.persist(novoProduto);
                    em.getTransaction().commit();

                    System.out.println("✅ Produto cadastrado com sucesso!");
                    break;

                case 5:
                    List<Client> dadosExportacao = em.createQuery("SELECT c FROM Client c", Client.class).getResultList();

                    System.out.println("Escolha o formato: 1 para JSON | 2 para CSV");
                    int formato = Integer.parseInt(scanner.nextLine());

                    if (formato == 1) {
                        reporterProcessor.process(new JSONReporter<>(dadosExportacao, "clientes_export.json"));
                    } else if (formato == 2) {
                        reporterProcessor.process(new CSVReporter<>(
                                dadosExportacao,
                                "clientes_export.csv",
                                new String[]{"ID", "Name", "Email"},
                                c -> new Object[]{c.getId(), c.getName(), c.getEmail()}
                        ));
                    } else {
                        System.out.println("Formato inválido.");
                    }
                    break;

                case 0:
                    executando = false;
                    System.out.println("Encerrando o sistema...");
                    break;

                default:
                    System.out.println("Opção inválida! Tente novamente.");
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