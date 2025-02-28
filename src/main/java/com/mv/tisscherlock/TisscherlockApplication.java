package com.mv.tisscherlock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.Banner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.boot.web.context.WebServerApplicationContext;

import java.util.logging.Logger;

@SpringBootApplication
@EnableAsync  // Habilita o uso de métodos assíncronos no Spring Boot
public class TisscherlockApplication {

    private static final Logger LOGGER = Logger.getLogger(TisscherlockApplication.class.getName());

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    public static void main(String[] args) {
        // Configura o Spring Boot para desabilitar o banner
        SpringApplication app = new SpringApplication(TisscherlockApplication.class);
        app.setBannerMode(Banner.Mode.OFF);

        try {
            // Log de inicialização
            LOGGER.info("Iniciando a aplicação Tisscherlock...");

            // Executa a aplicação Spring Boot com o perfil especificado
            ConfigurableApplicationContext context = app.run(args);
            
            // Log após inicialização bem-sucedida
            LOGGER.info("Aplicação Tisscherlock iniciada com sucesso.");

            // Logando o perfil ativo
            LOGGER.info("Perfil ativo: " + context.getEnvironment().getProperty("spring.profiles.active"));
            
            // Adiciona a inicialização dos beans personalizados ou outros ajustes, se necessário
            initializeCustomBeans(context);

        } catch (Exception e) {
            LOGGER.severe("Erro durante a inicialização da aplicação Tisscherlock: " + e.getMessage());
        }
    }

    /**
     * Este método é um exemplo de bean que pode ser inicializado na aplicação.
     * Aqui você pode configurar inicializações específicas, como setup de conexões de banco de dados.
     */
    private static void initializeCustomBeans(ConfigurableApplicationContext context) {
        LOGGER.info("Inicializando beans personalizados...");
        // Exemplo de inicialização de beans ou configurações personalizadas, como setup de banco de dados ou outras configurações
    }

    /**
     * Adicionando mais lógica de monitoramento e verificação de status na inicialização da aplicação.
     */
    private static void configureMonitoring() {
        LOGGER.info("Configurando monitoramento da aplicação...");
        // Aqui você pode configurar o Spring Boot Actuator ou outros monitoramentos, se necessário.
    }
}
