# Testes de Integração - E-commerce

## 📋 Visão Geral

Este diretório contém testes de integração completos para todos os endpoints POST da aplicação de e-commerce.

## 🛠️ Tecnologias e Ferramentas

### Ferramentas Principais
- **@SpringBootTest**: Carrega o contexto completo da aplicação Spring Boot
- **@AutoConfigureMockMvc**: Configura automaticamente o MockMvc para testes de API
- **@Testcontainers**: Gerencia containers Docker para testes (PostgreSQL)

### Dependências Adicionais Recomendadas
- **Testcontainers PostgreSQL**: Container específico para PostgreSQL
- **AssertJ**: Assertions fluentes e expressivas
- **Spring Security Test**: Suporte para testes com autenticação (@WithMockUser)

## 📦 Estrutura dos Testes

```
src/test/java/ecommerce/integration/
├── config/
│   └── AbstractIntegrationTest.java      # Classe base com configurações compartilhadas
└── controller/
    ├── AuthControllerIntegrationTest.java    # Testes de autenticação (login)
    ├── UserControllerIntegrationTest.java    # Testes de registro de usuários
    ├── ProductControllerIntegrationTest.java # Testes de criação de produtos
    ├── OrderControllerIntegrationTest.java   # Testes de criação de pedidos
    └── PaymentControllerIntegrationTest.java # Testes de processamento de pagamentos
```

## 🧪 Endpoints Testados

### 1. AuthController
- **POST /api/auth/login**
  - Login com credenciais válidas
  - Credenciais inválidas (senha incorreta)
  - Usuário não existente
  - Campos obrigatórios faltando
  - Login como MANAGER e CUSTOMER

### 2. UserController
- **POST /api/users/register**
  - Registro de usuário com sucesso
  - Registro como CUSTOMER (padrão)
  - Registro como MANAGER
  - Validação de campos obrigatórios
  - Validação de roles inválidas
  - Verificação de criptografia de senha

### 3. ProductController
- **POST /api/products/create**
  - Criação de produto com sucesso
  - Produtos com quantidade zero
  - Produtos com quantidade alta
  - Preços decimais
  - Validação de campos nulos
  - Autorização (apenas MANAGER)
  - Múltiplos produtos

### 4. OrderController
- **POST /api/order/create**
  - Criação de pedido com múltiplos itens
  - Pedido com item único
  - Cálculo correto do total
  - Redução de estoque
  - Agrupamento de pedidos (groupOrderId)
  - Lista de itens vazia
  - Autorização necessária

### 5. PaymentController
- **POST /api/payments/pay/{groupOrderId}**
  - Processamento de pagamento com sucesso
  - Cálculo correto do valor total
  - Múltiplos itens no pedido
  - Validação de paymentKey
  - Armazenamento de metadados
  - Autorização necessária

## 🔧 Configuração

### AbstractIntegrationTest

Classe base que fornece:
- Container PostgreSQL compartilhado entre todos os testes
- Configuração automática do MockMvc
- Limpeza do banco de dados entre os testes (@BeforeEach)
- Injeção de repositories para verificações

### Testcontainers

```java
@Container
protected static final PostgreSQLContainer<?> postgres = 
    new PostgreSQLContainer<>("postgres:15-alpine")
        .withDatabaseName("testdb")
        .withUsername("test")
        .withPassword("test");
```

- Usa PostgreSQL 15 Alpine (imagem leve)
- Container estático (compartilhado entre testes da mesma classe)
- Configuração dinâmica de propriedades do Spring

## ▶️ Como Executar

### Executar todos os testes
```bash
mvn test
```

### Executar testes de um controller específico
```bash
mvn test -Dtest=AuthControllerIntegrationTest
```

### Executar um teste específico
```bash
mvn test -Dtest=AuthControllerIntegrationTest#testLoginSuccess
```

## ✅ Boas Práticas Implementadas

1. **Isolamento de Testes**
   - Cada teste é independente
   - Banco de dados limpo entre testes
   - Dados de teste criados no @BeforeEach

2. **Nomenclatura Clara**
   - Padrão: `test<Action><Condition>`
   - Exemplo: `testLoginWithInvalidPassword`

3. **Cobertura Completa**
   - Casos de sucesso
   - Casos de erro (validações)
   - Casos de autorização
   - Casos limite

4. **Verificações Duplas**
   - Verifica resposta HTTP
   - Verifica dados no banco de dados

5. **Autenticação Realista**
   - Usa @WithMockUser para simular usuários autenticados
   - Testa cenários sem autenticação

## 📊 Métricas de Cobertura

- **5 Controllers testados**: 100%
- **5 Endpoints POST**: 100%
- **45+ casos de teste**: Cobrindo sucesso, falhas e casos limite

## 🚀 Sugestões de Melhorias

### Recomendadas
1. **AssertJ** - Já em uso, mas pode ser expandido
2. **Test Fixtures** - Criar factories para dados de teste
3. **Profiles de Teste** - Usar application-test.properties
4. **Relatórios de Cobertura** - JaCoCo para análise de cobertura

### Opcionais
1. **REST Assured** - Alternativa ao MockMvc (mais expressivo para APIs REST)
2. **WireMock** - Mockar serviços externos se houver integrações
3. **ArchUnit** - Testes arquiteturais
4. **Mutation Testing** - PITest para qualidade dos testes

## 🐳 Requisitos

- **Docker**: Necessário para executar Testcontainers
- **Java 17+**: Versão do projeto
- **Maven**: Para gerenciar dependências e executar testes

## 📝 Observações

- Os testes usam container real do PostgreSQL (não H2)
- Primeira execução pode demorar (download da imagem Docker)
- Container é automaticamente destruído após os testes
- Não é necessário ter PostgreSQL instalado localmente
