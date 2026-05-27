import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import models.Client;
import models.Product;

void main() {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
    EntityManager em = emf.createEntityManager();

    try {
        // CREATE
        em.getTransaction().begin();

        Client client1 = new Client("Luan Marx", "luan.menezes@gmail.com");
        Client client2 = new Client("Luanny Maria", "luanny.menezes@gmail.com");
        Product product1 = new Product("iPhone 17 Pro Max", 16500.00);
        Product product2 = new Product("Macbook Pro M5", 26500.00);

        em.persist(client1);
        em.persist(client2);
        em.persist(product1);
        em.persist(product2);

        em.getTransaction().commit();
        IO.println("Clientes e Produtos criados com sucesso!");

        IO.println();

        // READ ALL
        List<Client> clients = em.createQuery("SELECT c FROM Client c", Client.class).getResultList();
        IO.println("Todos os Clientes:");
        for (Client c : clients) {
            IO.println(" - " + c);
        }

        IO.println();

        List<Product> products = em.createQuery("SELECT p FROM Product p", Product.class).getResultList();
        IO.println("Todos os Produtos:");
        for (Product p : products) {
            IO.println(" - " + p);
        }

        IO.println();

        // READ BY ID
        Client foundClient = em.find(Client.class, client1.getId());
        IO.println("Buscando Cliente por ID: " + foundClient);

        IO.println();

        // UPDATE
        em.getTransaction().begin();
        foundClient.setName("Luan Marx Melo Menezes");
        em.getTransaction().commit();

        IO.println("Cliente atualizado: " + foundClient);

        IO.println();

        // REMOVE
        em.getTransaction().begin();
        Product productToRemove = em.find(Product.class, product1.getId());
        if (productToRemove != null) {
            em.remove(productToRemove);
            IO.println("Produto removido: " + productToRemove);
        }
        em.getTransaction().commit();

        IO.println();

        List<Product> productsAfterRemove = em.createQuery("SELECT p FROM Product p", Product.class).getResultList();
        IO.println("Todos os Produtos:");
        for (Product p : productsAfterRemove) {
            IO.println(" - " + p);
        }

    } catch (Exception e) {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        System.err.println("Error: " + e.getMessage());
    } finally {
        em.close();
        emf.close();
    }
}