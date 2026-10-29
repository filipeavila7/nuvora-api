# NuVora API - Plano de Ação e Roadmap Técnico (TODO.md)

Este documento representa o levantamento arquitetural completo e o backlog técnico executável para o desenvolvimento da API da **NuVora**. O projeto foi analisado detalhadamente a partir do código-fonte existente, identificando o estado real de cada componente, débitos técnicos, decisões pendentes e dependências entre módulos.

---

## 📊 Legenda de Status e Prioridades

### Status
- **[Concluído]**: Funcionalidade testada e validada no código.
- **[Parcialmente Implementado]**: Estrutura base criada, mas faltam fluxos críticos, validações ou regras de negócio.
- **[Pendente]**: Não implementado; apenas cascas vazias (interfaces/classes sem método) ou inexistente.
- **[Decisão Pendente]**: Depende de alinhamento prévio de regra de negócio/produto antes de codificar.
- **[Fora de Escopo]**: Identificado como desnecessário para o MVP/versão inicial.

### Prioridades
- **P0**: Bloqueia funcionalidades fundamentais ou envolve risco crítico de segurança, integridade de dados e concorrência.
- **P1**: Essencial para os fluxos centrais de e-commerce (compra, checkout, catálogo, pedidos).
- **P2**: Importante para a experiência do usuário ou operações auxiliares (avaliações, cupons, rastreamento).
- **P3**: Melhoria futura, refinamento ou otimização não bloqueante.

---

## 🗺️ Mapa de Dependências Técnicas

```
[Fase 1: Correções de Base & Segurança Global]
       │
       ▼
[Fase 2: Autenticação, Usuários, Perfis & Endereços]
       │
       ▼
[Fase 3: Catálogo, Categorias, Variantes & Estoque]
       │
       ▼
[Fase 4: Carrinho de Compras & Cálculo de Valores]
       │
       ▼
[Fase 5: Checkout, Pedidos & Itens do Pedido]
       │
       ▼
[Fase 6: Pagamentos (Stripe) & Webhooks Idempotentes]
       │
       ▼
[Fase 7: Funcionalidades Pós-Venda (Rastreio, Avaliações, Wishlist & Cupons)]
       │
       ▼
[Fase 8: Infraestrutura, Observabilidade, Testes & Deploy]
```

---

## Fase 1: Correções Arquiteturais de Base e Segurança Global

> **Objetivo:** Sanar inconsistências de modelagem JPA, unificar padrões de tratamento de erro, habilitar segurança granular e configurar suporte a banco de dados e CORS.

- [ ] **[P0]** **Configuração Global de Segurança e Acesso Web**
  - [ ] Implementar configuração de CORS explícita no `SecurityFilterChain` permitindo apenas origens autorizadas (frontend web/mobile), métodos HTTP permitidos e headers de autorização.
  - [ ] Habilitar `@EnableMethodSecurity` na classe de configuração de segurança para permitir uso de anotações como `@PreAuthorize("hasRole('ADMIN')")`.
  - [ ] Configurar um `AccessDeniedHandler` customizado retornando JSON formatado (HTTP 403 Forbidden com código `FORBIDDEN`), mantendo consistência com o `AuthenticationEntryPoint`.
  - [ ] Configurar rotas públicas no Spring Security: endpoints de leitura pública do catálogo (`GET /products/**`, `GET /categories/**`), webhooks da Stripe (`POST /payments/webhook`) e documentação Swagger (`/v3/api-docs/**`, `/swagger-ui/**`).

- [ ] **[P0]** **Auditoria e Correção de Entidades JPA e Modelagem de Dados**
  - [ ] Adicionar herança de `BaseEntity` nas entidades que ainda não a estendem para garantir auditoria temporal (`createdAt`, `updatedAt`): `User`, `Address`, `Profile`, `Category`, `Product`, `ProductVariant`, `Cart`, `CartItem`.
  - [ ] Corrigir cardinalidade de `User` ➔ `Address`:
    - Decisão no código atual: `User` possui `@OneToOne` com `Address`, enquanto e-commerces geralmente exigem `@OneToMany` com flag `isDefault`.
    - *Ação:* Ajustar para `@OneToMany` ou manter `@OneToOne` conforme decisão arquitetural formalizada.
  - [ ] Modelar relacionamento entre `OrderItem` e `ProductVariant`:
    - Atualmente `OrderItem` só possui quantidade, preço e `Order`, mas não referencia `ProductVariant` ou dados imutáveis do produto/SKU snapshot.
    - *Ação:* Adicionar relacionamento `@ManyToOne` com `ProductVariant` e campos de snapshot (`productName`, `sku`, `variantName`).
  - [ ] Corrigir entidade `Payment`:
    - Atualmente `Payment` possui `@OneToOne` bidirecional com `Order`, porém não há `@JoinColumn` mapeado no lado de `Order` e sem cascade claro.
  - [ ] Corrigir entidade `ProductKeyword`:
    - Atualmente possui apenas `@Id Long id`. Vincular com `Product` (`@ManyToMany` ou `@ElementCollection`) e campo de texto `keyword`.
  - [ ] Completar entidades mínimas vazias: `Cupom`, `Shipment`, `WishList`, `Notification`.

- [ ] **[P0]** **Substituição do `ddl-auto=update` por Migrations com Flyway**
  - [ ] Adicionar dependência do Flyway (`flyway-core`, `flyway-mysql`) no `pom.xml`.
  - [ ] Alterar `spring.jpa.hibernate.ddl-auto` para `validate` ou `none` no `application.properties`.
  - [ ] Criar script `V1__init_schema.sql` em `src/main/resources/db/migration` com todas as tabelas, índices e foreign keys do estado atual.
  - [ ] Criar índices para chaves estrangeiras e buscas frequentes (`sku`, `email`, `status`, `order_id`, `category_id`).

- [ ] **[P1]** **Refinamento do Tratamento Global de Exceções (`GlobalExceptionHandler`)**
  - [ ] Adicionar handler para `DataIntegrityViolationException` e `DuplicateKeyException` para retornar HTTP 409 Conflict em violações de unique constraints sem vazar detalhes internos do banco.
  - [ ] Adicionar handler para `HttpMessageNotReadableException` (JSON malformado ou tipos inválidos) retornando HTTP 400 Bad Request.
  - [ ] Adicionar handler para `AccessDeniedException` retornando HTTP 403 Forbidden padronizado.
  - [ ] Criar hierarquia de exceções de domínio do projeto (`ResourceNotFoundException`, `BusinessRuleException`, `StockUnavailableException`, `UnauthorizedOperationException`) em substituição ao uso cru de `ResponseStatusException` nos services.

---

## Fase 2: Autenticação, Usuários, Perfis e Endereços

> **Objetivo:** Consolidar o ciclo de vida do usuário, segurança de dados pessoais (LGPD), recuperação de conta e gestão de múltiplos endereços com propriedade estrita.

- [ ] **[P0]** **Módulo de Usuários e Gestão de Contas (`user`)**
  - Estado Atual: `User` possui entidade básica. `UserService` implementa apenas criação simples duplicando parte do `AuthService`. `UserController` está vazio.
  - [ ] Criar endpoint `GET /users/me` para consulta dos dados do usuário autenticado a partir do token JWT.
  - [ ] Criar endpoint `PUT /users/me` com DTO de atualização (`name`, etc.).
  - [ ] Implementar fluxo de alteração de senha autenticada (`PUT /users/me/password`):
    - Validar senha atual via `passwordEncoder.matches`.
    - Garantir força da nova senha e impedir repetição imediata.
  - [ ] Implementar desativação lógica de conta (`DELETE /users/me`):
    - Adicionar flag `boolean active = true` na entidade `User`.
    - Bloquear login de contas inativas no `AuthService` e `CustomUserDetailsService`.
  - [ ] Unificar responsabilidade de cadastro entre `AuthService.register()` e `UserService.createUser()`.
  - [ ] Implementar endpoints administrativos para listagem, bloqueio e atribuição de perfis de usuário (`GET /admin/users`, `PATCH /admin/users/{id}/role`).

- [ ] **[P1]** **Módulo de Autenticação Avançada e Recuperação de Acesso (`auth` / `refreshToken`)**
  - Estado Atual: Login, register, refresh com rotação criptografada via SHA-256 e logout com revogação implementados.
  - [ ] Implementar proteção contra reuso de Refresh Token:
    - Se um refresh token já revogado for apresentado, revogar imediatamente toda a árvore de tokens daquele usuário (detecção de roubo de credencial).
  - [ ] Criar rotina agendada (`@Scheduled`) para expurgo de refresh tokens revogados e expirados no banco de dados.
  - [ ] Implementar fluxo de recuperação de senha ("Esqueci minha senha"):
    - [ ] Criar entidade `PasswordResetToken` (token aleatório, hash no banco, expiração de 15 a 30 minutos).
    - [ ] Criar endpoint `POST /auth/forgot-password` (solicitação por email).
    - [ ] Criar endpoint `POST /auth/reset-password` (envio do token e nova senha).
  - [ ] Decisão Pendente: Definição do provedor de envio de email transacional (SendGrid, Amazon SES, Mailgun ou Spring Mail SMTP).

- [ ] **[P1]** **Módulo de Perfil e Dados Pessoais (`profile`)**
  - Estado Atual: Entidade `Profile` possui `cpf`, `profileUrlImg` e relacionamento `@OneToOne` com `User`. Service, Controller e DTOs vazios.
  - [ ] Implementar `ProfileRequest` com validação de CPF (algoritmo `@CPF` do Hibernate Validator ou validador customizado).
  - [ ] Implementar `ProfileResponse` contendo dados públicos do perfil.
  - [ ] Implementar `ProfileService`:
    - Métodos para criar/atualizar perfil do usuário autenticado.
    - Validação de unicidade de CPF ativo no sistema.
    - Sanitização e mascaramento de CPF na resposta de endpoints convencionais.
  - [ ] Implementar endpoints em `ProfileController` com verificação de autorização:
    - `GET /profile/me`
    - `PUT /profile/me`
    - `POST /profile/me/avatar` (integração com serviço de upload de imagens).

- [ ] **[P1]** **Módulo de Endereços (`address`)**
  - Estado Atual: Entidade `Address` criada. `AddressService`, `AddressController` e DTOs estão vazios.
  - [ ] Implementar `AddressRequest` com validações Bean Validation (`street`, `number`, `neighborhood`, `city`, `state`, `zipCode` no formato `\\d{5}-?\\d{3}`).
  - [ ] Implementar `AddressResponse`.
  - [ ] Decisão de Arquitetura: Usuário terá apenas 1 endereço fixo ou múltiplos endereços (com flag `isDefault`)?
  - [ ] Implementar `AddressService`:
    - Método para cadastrar endereço vinculado estritamente ao usuário do contexto de segurança.
    - Método para listar endereço(s) do usuário.
    - Método para atualizar endereço garantindo que o usuário só altere seu próprio endereço (`Ownership Verification`).
    - Método para exclusão de endereço com verificação de integridade (impedir exclusão caso haja pedidos vinculados que dependam dele).
  - [ ] Implementar `AddressController` com mapeamento dos endpoints RESTful seguros.

---

## Fase 3: Catálogo de Produtos, Categorias, Variantes e Estoque

> **Objetivo:** Construir o catálogo de produtos com controle fino de variantes (tamanho, cor, preço individual, estoque), categorização e busca pública performática.

- [ ] **[P0]** **Módulo de Categorias (`category`)**
  - Estado Atual: Entidade `Category` básica (`id`, `name`, `active`, lista de produtos). Demais classes vazias.
  - [ ] Definir `CategoryRequest` com campos: `name` (not blank, max 100), `slug` (opcional ou gerado), `active`.
  - [ ] Definir `CategoryResponse`.
  - [ ] Implementar `CategoryService`:
    - Cadastrar categoria com validação de unicidade de nome.
    - Atualizar dados de categoria.
    - Desativação lógica (`soft delete`) ao invés de deleção física se houver produtos vinculados.
    - Listagem pública de categorias ativas (`GET /categories`).
    - Listagem administrativa com filtros e paginação (`GET /admin/categories`).
  - [ ] Implementar `CategoryController` separando rotas públicas de rotas restritas a `ADMIN`.

- [ ] **[P0]** **Módulo de Produtos (`product`)**
  - Estado Atual: Entidade `Product` básica com relacionamento para `ProductVariant` e `Category`. DTOs e Service vazios.
  - [ ] Definir `ProductRequest` contendo: `name`, `description`, `categoryId`, e opcionalmente variantes iniciais.
  - [ ] Definir `ProductResponse` contendo resumo do produto, categoria e lista de variantes disponíveis.
  - [ ] Implementar `ProductService`:
    - Cadastrar produto validando existência e status ativo da categoria informada.
    - Atualizar produto e gerenciar associação com categorias.
    - Desativação lógica de produtos (`active = false`), propagando para suas variantes.
    - Consulta paginada pública (`GET /products`): filtros por categoria, termo de busca, ordenação por preço e novidades.
    - Consulta detalhada por ID ou Slug (`GET /products/{id}`).
  - [ ] Implementar `ProductController` com endpoints públicos de leitura e endpoints protegidos com `ROLE_ADMIN` para mutações.

- [ ] **[P0]** **Módulo de Variantes de Produto e Controle de Estoque (`productVariant`)**
  - Estado Atual: Entidade possui `sku`, `color`, `size`, `price`, `stockQuantity`, `isDefault`, `active`, `product`. DTOs e Service vazios.
  - [ ] Definir `ProductVariantRequest` com validações rigorosas:
    - `@NotBlank` no `sku`, `@Positive` no `price`, `@Min(0)` no `stockQuantity`.
  - [ ] Definir `ProductVariantResponse`.
  - [ ] Implementar `ProductVariantService`:
    - Criação de variantes garantindo unicidade global do `sku`.
    - Regra de negócio: garantir que haja apenas uma variante marcada como `isDefault` por produto.
    - Atualização de preço e dados cadastrais.
    - Atualização manual e ajuste de inventário de estoque (adicionar/remover itens de inventário).
    - Método atômico para baixa e reserva de estoque (`decrementStock`) com tratamento de concorrência.
    - Método para devolução de estoque (`incrementStock`) em caso de cancelamento de pedido ou pagamento recusado.
  - [ ] Implementar `ProductVariantController`.

- [ ] **[P1]** **Módulo de Imagens de Produtos (`productImg`) e Uploads**
  - Estado Atual: Entidade com `product`, `imageUrl`, `isPrimary`. Demais classes vazias. Pacote `upload` não possui arquivos.
  - [ ] Decisão Pendente: Onde serão armazenadas as imagens? (Amazon S3, Google Cloud Storage, Cloudinary ou armazenamento local estático).
  - [ ] Criar serviço de armazenamento de arquivos (`StorageService` ou `CloudinaryService`) no pacote `upload`.
  - [ ] Validar tipos MIME permitidos (`image/jpeg`, `image/png`, `image/webp`) e limite máximo de tamanho de arquivo (ex: 5MB).
  - [ ] Implementar upload e vinculação de imagens ao produto:
    - Endpoint `POST /products/{id}/images` aceitando `MultipartFile`.
    - Garantir regra: apenas 1 imagem primária por produto (`isPrimary = true`).
    - Remoção de imagens e expurgo do provedor de storage.

- [ ] **[P2]** **Módulo de Palavras-Chave de Produtos (`productKeywords`)**
  - Estado Atual: Entidade possui apenas `id`.
  - [ ] Decidir se `ProductKeyword` será mantido como entidade dedicada ou substituído por `@ElementCollection` de tags no `Product`.
  - [ ] Se mantida como entidade: criar campos `keyword` (normalizado em minúsculas), vincular a `Product`, implementar repositório e busca por tags.

---

## Fase 4: Carrinho de Compras (`cart` e `cartItens`)

> **Objetivo:** Oferecer gestão íntegra de carrinho para o cliente autenticado, validando estoques, preços correntes e cálculos em tempo real no backend.

- [ ] **[P0]** **Módulo de Itens e Carrinho (`cart` / `cartItens`)**
  - Estado Atual: Entidades `Cart` e `CartItem` possuem mapeamentos JPA básicos. Services, DTOs e Controllers estão vazios.
  - [ ] Implementar `CartItemRequest` contendo `productVariantId` e `quantity` (`@Min(1)`).
  - [ ] Implementar `CartResponse` e `CartItemResponse` contendo detalhamento de preços correntes, subtotais e estoque disponível no momento da consulta.
  - [ ] Implementar `CartService`:
    - Obter ou criar automaticamente o carrinho do usuário autenticado (`getOrCreateCartForUser`).
    - Adicionar item ao carrinho:
      - Validar se a variante existe e está ativa (`active = true`).
      - Validar se o produto pai está ativo.
      - Validar disponibilidade de estoque imediata (`stockQuantity >= requestedQuantity`).
      - Se a variante já existir no carrinho, somar a quantidade respeitando o teto de estoque.
    - Atualizar quantidade de um item no carrinho (`PATCH /cart/items/{itemId}`):
      - Revalidar disponibilidade de estoque.
      - Se quantidade informada for 0, remover o item.
    - Remover item específico do carrinho (`DELETE /cart/items/{itemId}`):
      - Verificar se o item pertence realmente ao carrinho do usuário autenticado.
    - Limpar carrinho (`DELETE /cart`):
      - Remover todos os itens mantendo a casca do carrinho.
    - Calcular totais e subtotais sempre dinamicamente a partir dos preços atuais de `ProductVariant.price` no backend, nunca aceitando preços vindos do cliente.
  - [ ] Implementar endpoints em `CartController`:
    - `GET /cart`
    - `POST /cart/items`
    - `PATCH /cart/items/{id}`
    - `DELETE /cart/items/{id}`
    - `DELETE /cart`
  - [ ] Eliminar ou redirecionar endpoints duplicados em `CartItemController` para concentrar a API em `/cart/**`.

---

## Fase 5: Checkout, Pedidos e Transições de Estado

> **Objetivo:** Processamento transacional de checkout, criação de snapshot imutável de itens/preços, reserva segura de estoque e orquestração dos status de pedido.

- [ ] **[P0]** **Modelagem Imutável de Pedidos e Itens (`order` / `orderItens`)**
  - Estado Atual: Entidade `Order` com campos de total, desconto, status e `orderItems`. `OrderItem` possui preço e quantidade, mas falta referência aos dados históricos da variante.
  - [ ] Ajustar `OrderItem` para guardar snapshot dos dados do produto no instante da compra:
    - Adicionar `variantId`, `skuSnapshot`, `productNameSnapshot`, `variantNameSnapshot`, `unitPrice`.
    - Garantir que alterações futuras no catálogo de produtos não impactem dados de pedidos já fechados.

- [ ] **[P0]** **Fluxo Transacional de Criação de Pedido (Checkout)**
  - [ ] Criar `CheckoutRequest` contendo: `addressId`, dados de frete selecionados e cupom de desconto (opcional).
  - [ ] Criar `OrderResponse` detalhado com status, itens, snapshots, total e endereçamento.
  - [ ] Implementar método `@Transactional` no `OrderService` para criação de pedidos:
    - 1. Recuperar carrinho do usuário autenticado; validar se o carrinho não está vazio.
    - 2. Validar endereço de entrega: confirmar se pertence ao usuário autenticado.
    - 3. Bloquear variantes no banco com lock pessimista (`Pessimistic Lock`) ou tratamento de versão otimista (`Optimistic Lock`) para evitar condições de corrida (*Race Conditions*) no estoque.
    - 4. Validar se todos os itens possuem estoque suficiente.
    - 5. Abater as quantidades do estoque das variantes.
    - 6. Calcular subtotal, aplicar desconto de cupom (se válido e elegível) e frete.
    - 7. Persistir `Order` com status `OrderStatus.PENDING`.
    - 8. Gerar os registros de `OrderItem` com snapshot imutável dos valores.
    - 9. Limpar os itens do carrinho do usuário.
    - 10. Iniciar a criação da intenção de pagamento na Stripe.

- [ ] **[P0]** **Máquina de Estados e Transições de Pedido (`OrderStatus`)**
  - Estado Atual: Enum `OrderStatus` possui `PENDING`, `PAID`, `PROCESSING`, `SHIPPED`, `DELIVERED`, `CANCELED`, `PAYMENT_FAILED`.
  - [ ] Criar classe ou validador de transição de status para impedir saltos ilegais (ex: de `CANCELED` para `SHIPPED`, ou de `DELIVERED` para `PENDING`).
  - [ ] Implementar cancelamento de pedido (`cancelOrder`):
    - Apenas pedidos com status permitidos (`PENDING`, `PAYMENT_FAILED`, ou `PAID` antes de expedição) podem ser cancelados.
    - Devolver estoque de cada item do pedido atomicamente para as respectivas variantes (`incrementStock`).
    - Se o pedido já estava pago, acionar estorno no Stripe.

- [ ] **[P1]** **Endpoints de Consulta e Gestão de Pedidos (`OrderController`)**
  - [ ] `POST /orders/checkout` - Realizar o fechamento do pedido a partir do carrinho.
  - [ ] `GET /orders/me` - Listagem paginada dos pedidos do cliente autenticado com filtros de data e status.
  - [ ] `GET /orders/me/{id}` - Detalhes do pedido com validação estrita de posse (`Ownership Check`).
  - [ ] `PATCH /orders/me/{id}/cancel` - Cancelamento solicitado pelo próprio cliente (se respeitadas as janelas permitidas).
  - [ ] `GET /admin/orders` - Visão administrativa de pedidos com filtros avançados.
  - [ ] `PATCH /admin/orders/{id}/status` - Atualização manual de status de pedido por operadores autorizados (`ROLE_ADMIN`).

---

## Fase 6: Pagamentos (Stripe) e Webhooks Idempotentes

> **Objetivo:** Integrar gateway Stripe para cobrança segura, criação de `PaymentIntent`, processamento de webhooks com verificação criptográfica de assinatura e controle de idempotência.

- [ ] **[P0]** **Configuração e Integração com a SDK da Stripe**
  - [ ] Adicionar dependência oficial da Stripe no `pom.xml` (`com.stripe:stripe-java:latest`).
  - [ ] Adicionar propriedades de configuração no `application.properties`:
    - `stripe.api-key=${STRIPE_SECRET_KEY}`
    - `stripe.webhook-secret=${STRIPE_WEBHOOK_SECRET}`
  - [ ] Criar classe de configuração Spring para inicializar a chave secreta da Stripe no startup.

- [ ] **[P0]** **Geração de Pagamento (`PaymentIntent`)**
  - [ ] Implementar `PaymentService.createPaymentIntent(Order order)`:
    - Converter o valor total do pedido para a menor unidade monetária da moeda (ex: centavos em BRL/USD).
    - Criar `PaymentIntent` via SDK da Stripe incluindo nos `metadata` o `orderId` e `userId`.
    - Persistir a entidade `Payment` com status `PaymentStatus.PENDING`, vinculada à `Order` e com o identificador retornado `stripePaymentIntentId`.
    - Retornar o `clientSecret` para o frontend inicializar o Stripe Elements / checkout seguro.

- [ ] **[P0]** **Recepção e Processamento Idempotente de Webhooks da Stripe**
  - [ ] Criar endpoint público `POST /payments/webhook`:
    - Receber payload bruto (`String payload`) e header `Stripe-Signature`.
    - Validar a assinatura da mensagem usando `Webhook.constructEvent()` com o `STRIPE_WEBHOOK_SECRET`. Rejeitar requisições sem assinatura válida com HTTP 400 Bad Request.
  - [ ] Implementar controle de Idempotência:
    - Criar entidade ou tabela `ProcessedWebhookEvent` (`eventId`, `eventType`, `processedAt`) com chave única em `eventId`.
    - Se o `eventId` já foi processado anteriormente, retornar imediatamente HTTP 200 OK sem reprocessar regras de negócio.
  - [ ] Tratar eventos críticos da Stripe:
    - `payment_intent.succeeded`:
      - Atualizar `Payment.status` para `SUCCEEDED`.
      - Atualizar `Order.status` para `PAID`.
      - Disparar evento de notificação e início de separação.
    - `payment_intent.payment_failed`:
      - Atualizar `Payment.status` para `FAILED`.
      - Atualizar `Order.status` para `PAYMENT_FAILED`.
      - Devolver estoque previamente reservado das variantes (`incrementStock`).
    - `payment_intent.canceled`:
      - Atualizar `Payment.status` para `CANCELED` e `Order.status` para `CANCELED`.
      - Devolver estoque.
    - `charge.refunded`:
      - Atualizar `Payment.status` para `REFUNDED`.

- [ ] **[P1]** **Fluxo de Reembolso (`Refund`)**
  - [ ] Implementar método para estorno de pagamento no `PaymentService` (`Stripe Refund API`).
  - [ ] Registrar histórico de estorno e atualizar status no banco.

---

## Fase 7: Pós-Venda, Avaliações, Rastreamento, Cupons e Wishlist

> **Objetivo:** Implementar regras de engajamento do cliente, feedback de produtos com compras verificadas e rastreabilidade da entrega.

- [ ] **[P1]** **Módulo de Avaliações e Imagens (`review` / `reviewImg`)**
  - Estado Atual: Entidades `Review` e `ReviewImg` criadas com campos úteis (`rating`, `comment`, `user`, `productVariant`, lista de imagens). Services, DTOs e Controllers vazios.
  - [ ] Definir `ReviewRequest` com validações:
    - `@NotNull @Min(1) @Max(5) Integer rating`
    - `@NotBlank @Size(min = 5, max = 1000) String comment`
  - [ ] Implementar `ReviewResponse` com dados do autor, data, nota, comentário e URLs de imagens.
  - [ ] Implementar regras de negócio no `ReviewService`:
    - **Compra Verificada:** Verificar se o usuário autenticado realmente comprou a variante em um pedido com status `DELIVERED` ou `PAID`.
    - **Anti-Duplicação:** Impedir que o mesmo usuário crie mais de uma avaliação para a mesma variante (verificar constraint única em `[user_id, product_variant_id]`).
    - Edição e exclusão de avaliação permitidas apenas para o próprio autor ou `ROLE_ADMIN`.
    - Upload de fotos na avaliação (limite configurável, ex: até 3 imagens por review).
  - [ ] Implementar endpoints em `ReviewController`:
    - `GET /products/{productId}/reviews` (leitura pública com paginação e média de notas).
    - `POST /variants/{variantId}/reviews` (criação autenticada).
    - `PUT /reviews/{id}`
    - `DELETE /reviews/{id}`

- [ ] **[P1]** **Módulo de Rastreamento de Envios (`shipment` / `shipmentEvent`)**
  - Estado Atual: `ShipmentEvent` possui dados de evento, data, localização e status. `Shipment` está vazio.
  - [ ] Estruturar entidade `Shipment`:
    - Campos: `trackingNumber`, `carrier`, `estimatedDeliveryDate`, `status`, relacionamento `@OneToOne` com `Order` e `@OneToMany` com `ShipmentEvent`.
  - [ ] Implementar `ShipmentService`:
    - Criação de remessa quando o pedido muda para `SHIPPED`.
    - Inclusão de eventos de rastreio (`ShipmentEvent`).
    - Consulta de rastreamento pelo cliente autenticado (`GET /orders/{orderId}/tracking`).
    - Atualização de eventos por operadores administrativos (`POST /admin/shipments/{id}/events`).

- [ ] **[P2]** **Módulo de Cupons e Descontos (`cupom` / `userCupom` / `promotions`)**
  - Estado Atual: Entidades `Cupom`, `Promotion` e `WishList` vazias. `UserCupom` possui campos `used`, `usedAt`, `user`, `cupom`.
  - [ ] Estruturar entidade `Cupom`:
    - Campos: `code` (único, letras maiúsculas), `discountType` (PERCENTAGE, FIXED_VALUE), `discountValue`, `minOrderValue`, `startDate`, `expirationDate`, `usageLimit`, `usageCount`, `active`.
  - [ ] Implementar `CupomService`:
    - Validação de código de cupom na simulação do carrinho/checkout.
    - Impedir reutilização se o cupom for de uso único por cliente via `UserCupom`.
    - Atualização atômica do contador de usos.
  - [ ] Decisão Pendente: Módulo `promotions` terá regras de descontos automáticos por categoria ou continuará simplificado apenas via cupom?

- [ ] **[P2]** **Módulo de Lista de Desejos (`wishList`)**
  - [ ] Estruturar entidade `WishList`: vincular `User` com lista de `ProductVariant` ou `Product`.
  - [ ] Implementar endpoints para adicionar, remover e listar itens favoritos do usuário autenticado.

- [ ] **[P3]** **Módulo de Notificações (`notifications`)**
  - [ ] Estruturar entidade `Notification`: `user`, `title`, `message`, `read`, `createdAt`.
  - [ ] Implementar disparos assíncronos (`@Async`) ou eventos de domínio para notificar o cliente sobre: pedido recebido, pagamento aprovado, pedido enviado e entrega realizada.

---

## Fase 8: Qualidade, Testes, Observabilidade e Preparação para Produção

> **Objetivo:** Cobrir regras críticas com suíte de testes automatizados, documentar API com OpenAPI 3 e padronizar o empacotamento para nuvem/Docker.

- [ ] **[P0]** **Testes Unitários de Regras de Negócio Críticas (JUnit 5 & Mockito)**
  - Estado Atual: Existe apenas a classe `NuvoraApplicationTests` vazia.
  - [ ] Testes unitários de `AuthService` e `RefreshTokenService` (fluxos de login com credenciais válidas e inválidas, renovação de token expirado/revogado, rotação de tokens).
  - [ ] Testes unitários de `OrderService` (cálculo de descontos, validação de itens esgotados, transições inválidas de status).
  - [ ] Testes unitários de concorrência e baixa de estoque em `ProductVariantService`.
  - [ ] Testes unitários do webhook da Stripe em `PaymentService` (verificação de assinatura, idempotência em eventos repetidos).

- [ ] **[P1]** **Testes de Integração com Banco de Dados e Testcontainers**
  - [ ] Configurar Testcontainers com MySQL ou perfil com H2 Database para suíte de testes de integração.
  - [ ] Testes de fluxo ponta a ponta com `MockMvc` ou `TestRestTemplate`:
    - Cadastro ➔ Login ➔ Adição ao Carrinho ➔ Checkout ➔ Simulação de Webhook Stripe Pago ➔ Confirmação do Pedido.
  - [ ] Testes de segurança: validar bloqueio de acesso a recursos alheios (IDOR - Insecure Direct Object References) e endpoints administrativos sem role adequada.

- [ ] **[P1]** **Documentação Interativa com OpenAPI / Swagger**
  - [ ] Adicionar dependência `springdoc-openapi-starter-webmvc-ui` no `pom.xml`.
  - [ ] Configurar metadados da API (título, descrição, versão, segurança Bearer JWT global).
  - [ ] Anotar controllers com `@Tag`, `@Operation`, `@ApiResponse` detalhando códigos de erro e schemas dos DTOs.

- [ ] **[P1]** **Configuração de Ambientes e Segurança Operacional**
  - [ ] Criar profiles Spring segregados: `application-dev.properties`, `application-test.properties`, `application-prod.properties`.
  - [ ] Remover segredos hardcoded ou valores default fracos; garantir leitura estrita via variáveis de ambiente.
  - [ ] Configurar logs estruturados sem exposição de dados sensíveis (senhas, cartões, dados brutos de refresh tokens ou CPF).

- [ ] **[P2]** **Containerização e Docker**
  - [ ] Criar `Dockerfile` multi-stage build para compilação otimizada com OpenJDK 21.
  - [ ] Criar `docker-compose.yml` contendo a aplicação NuVora e o banco de dados MySQL 8 pronto para execução local.

---

## 📌 Resumo de Decisões de Negócio e Arquitetura Pendentes

1. **Gestão de Endereços:** O sistema permitirá apenas 1 endereço por usuário (atual `@OneToOne`) ou múltiplos endereços com seleção de padrão (`@OneToMany`)? *(Recomendado: Múltiplos endereços)*.
2. **Snapshot de Dados no Pedido:** O `OrderItem` armazenará dados em colunas explícitas (`sku`, `productName`, `unitPrice`) ou como um documento/JSON embutido? *(Recomendado: Colunas explícitas relacionando com ProductVariant)*.
3. **Provedor de Armazenamento de Arquivos:** Qual serviço será utilizado para persistência das fotos de produtos e fotos de avaliações? (Cloudinary, AWS S3, Google Cloud Storage ou MinIO local).
4. **Envio de Emails Transacionais:** Haverá serviço integrado para recuperação de senha e atualização de status de pedidos no MVP? Qual serviço utilizar?
5. **Diferenciação entre Cupons e Promoções:** Manter o pacote `promotions` para descontos percentuais automáticos por categoria ou unificar sob o domínio de `cupom`?
6. **Política de Avaliação de Compras:** Apenas usuários que receberam o produto (`DELIVERED`) poderão avaliar, ou basta o pedido estar pago (`PAID`)?
