package com.xflprflx.table_poc;

import com.xflprflx.table_poc.annotations.Entidade;
import com.xflprflx.table_poc.core.SchemaGenerator;
import com.xflprflx.table_poc.core.SimpleEntityManager;
import com.xflprflx.table_poc.entities.Produto;
import com.xflprflx.table_poc.entities.Usuario;
import org.reflections.Reflections;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Set;

@SpringBootApplication
public class TablePocApplication implements CommandLineRunner {

    private final SchemaGenerator schemaGenerator;
    private final SimpleEntityManager entityManager;

    public TablePocApplication(SchemaGenerator schemaGenerator, SimpleEntityManager entityManager) {
        this.schemaGenerator = schemaGenerator;
        this.entityManager = entityManager;
    }

    public static void main(String[] args) {
        SpringApplication.run(TablePocApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("==================================================");
        System.out.println("INICIANDO GERADOR DE ESQUEMA DO BANCO DE DADOS...");
        System.out.println("==================================================");

        final String ENTITIES_PACKAGE = "com.xflprflx.table_poc";
        Reflections reflections = new Reflections(ENTITIES_PACKAGE);
        Set<Class<?>> entityClasses = reflections.getTypesAnnotatedWith(Entidade.class);

        for (Class<?> entityClass : entityClasses) {
            schemaGenerator.generateTable(entityClass);
            System.out.println("---");
        }

        System.out.println("==================================================");
        System.out.println("GERAÇÃO DE ESQUEMA CONCLUÍDA.");
        System.out.println("==================================================");

        System.out.println("\n==================================================");
        System.out.println("INICIANDO PERSISTÊNCIA DE DADOS...");

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome("Ada Lovelace");
        novoUsuario.setEmail("ada.lovelace@example.com");
        novoUsuario.setIdade(36);
        System.out.println("Objeto antes de persistir: " + novoUsuario);

        Usuario usuarioSalvo = entityManager.persist(novoUsuario);
        System.out.println("Objeto depois de persistir: " + usuarioSalvo);

        System.out.println("---");

        Produto novoProduto = new Produto();
        novoProduto.setNome("Máquina Diferencial");
        novoProduto.setPreco(1842.00);
        System.out.println("Objeto antes de persistir: " + novoProduto);

        Produto produtoSalvo = entityManager.persist(novoProduto);
        System.out.println("Objeto depois de persistir: " + produtoSalvo);

        System.out.println("PERSISTÊNCIA DE DADOS CONCLUÍDA.");
        System.out.println("==================================================");
    }
}
