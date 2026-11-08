# 🏛️ Easy Reserve API — Reserva Fácil (SIGEA)

> **API RESTful Multi-Tenant SaaS** para Gestão e Reserva Inteligente de Ambientes Físicos, Salas, Equipamentos e Chaves Institucionais.

---

## 📌 Visão Geral

O **Easy Reserve API** é o núcleo de backend do sistema de reserva de espaços físicos para instituições públicas, órgãos governamentais e corporações. Desenvolvido com **Spring Boot 3.5**, a arquitetura implementa isolamento multi-tenant seguro por Workspace, controle de acesso baseado em papéis (RBAC com JWT), histórico de auditoria completo e um **motor inteligente de recomendação de ambientes** com pesos customizáveis por usuário.

---

## 🚀 Tecnologias Utilizadas

- **Linguagem:** Java 17 LTS
- **Framework:** Spring Boot 3.5+
- **Segurança:** Spring Security 6 + JJWT (`0.11.5`) com Roles (`COMMON`, `ADMIN`, `OWNER`)
- **Persistência:** Spring Data JPA / Hibernate
- **Banco de Dados:** PostgreSQL 16+
- **Migrações:** Flyway Migration (V1 a V12)
- **Documentação:** SpringDoc OpenAPI 3.1 & Swagger UI
- **Produtividade:** Lombok & Bean Validation (`jakarta.validation`)

---

## 🧩 Principais Módulos & Recursos

| Módulo | Código | Descrição |
| :--- | :--- | :--- |
| **Workspaces** | Multi-Tenant | Isolamento total de dados entre órgãos/empresas clientes |
| **Autenticação & Usuários** | `MOD-01` | Registro institucional, Login JWT, RBAC e Perfil do Usuário |
| **Ambientes Físicos** | `MOD-02` | Cadastro de salas, auditórios, laboratórios e capacidade |
| **Elementos & Recursos** | `MOD-03` | Catálogo de equipamentos (projetores, ar-condicionado, lousas) |
| **Controle de Chaves** | `MOD-04` | Gestão de chaveiros, numeração física e disponibilidade |
| **Módulos Ativos** | `MOD-05` | Ativação/desativação modular de features por workspace |
| **Reservas & Planner** | `MOD-06` | Agendamento de horários, detecção de conflitos e vinculação de chaves |
| **Recomendação & Perfil**| `MOD-07` | Motor de pontuação com pesos de recursos definidos no formulário e edição de perfil próprio |
| **Histórico & Auditoria**| `AUDIT` | Rastreabilidade completa de ações de reserva (criação, cancelamento, devolução) |

---

## 🧠 Motor de Recomendação Inteligente

O sistema conta com um algoritmo que analisa todos os ambientes disponíveis no workspace e ranqueia as melhores opções para o usuário baseado em:
- **Capacidade mínima requerida de pessoas**;
- **Compatibilidade de recursos presentes na sala**;
- **Pesos de relevância atribuídos dinamicamente pelo próprio usuário** na hora da solicitação (ex: Ar-Condicionado com peso 10, Microfone com peso 3).

---

## ⚙️ Pré-Requisitos e Execução Local

### Pré-Requisitos:
- **Java 17+** (JDK)
- **PostgreSQL 14+** em execução

### 1. Configurar Banco de Dados
Crie a base de dados no PostgreSQL:
```sql
CREATE DATABASE easy_reserve;
```

Ajuste as credenciais em `src/main/resources/application.properties` se necessário:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/easy_reserve
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### 2. Executar a Aplicação
No terminal do projeto:
```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

A API iniciará por padrão na porta **`9011`**.

---

## 📖 Documentação da API (OpenAPI / Swagger)

Após iniciar a aplicação, a documentação interativa estará disponível em:
- **Swagger UI:** `http://localhost:9011/swagger-ui.html`
- **OpenAPI JSON:** `http://localhost:9011/v3/api-docs`

---

## 🌐 Produção

- **Endpoint Base:** `https://easy-reserve-api.72-60-158-216.sslip.io/api/v1`
- **Swagger em Produção:** `https://easy-reserve-api.72-60-158-216.sslip.io/swagger-ui.html`
