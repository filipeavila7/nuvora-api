# Testes Automatizados em Java — JUnit 5, Mockito e Spring Boot

## 1. Introdução aos testes automatizados

Testes automatizados são códigos escritos para verificar se determinadas funcionalidades da aplicação estão funcionando conforme o esperado.

Em vez de executar manualmente cada funcionalidade, criamos testes que executam cenários automaticamente e verificam seus resultados.

### Por que testar?

- Identificar erros antes que cheguem à produção.
- Evitar que alterações quebrem funcionalidades existentes.
- Garantir que as regras de negócio sejam respeitadas.
- Verificar se exceções são lançadas corretamente.
- Facilitar refatorações com mais segurança.
- Documentar o comportamento esperado do sistema.

**Importante:** testar não significa garantir que o sistema nunca terá erros. Significa verificar automaticamente os comportamentos mais importantes da aplicação.

### O que devemos testar?

Em uma aplicação de ecommerce, por exemplo:

- Impedir cadastro com e-mail já existente.
- Garantir que senhas sejam armazenadas com hash.
- Impedir compras com quantidade superior ao estoque.
- Validar cálculos de pedidos e descontos.
- Impedir que um usuário acesse pedidos de outra pessoa.
- Garantir que um pedido não seja pago duas vezes.

O foco deve estar nas regras de negócio e nos comportamentos importantes, não apenas na quantidade de testes.

---

## 2. Principais ferramentas

### JUnit 5

O JUnit é um framework utilizado para criar, organizar e executar testes automatizados em Java.

Principais recursos:

| Recurso | Funcionalidade |
|---|---|
| `@Test` | Identifica um método como teste. |
| `@BeforeEach` | Executa antes de cada teste. |
| `@AfterEach` | Executa depois de cada teste. |
| `@DisplayName` | Define um nome descritivo para o teste. |
| `assertEquals()` | Compara o resultado esperado com o obtido. |
| `assertTrue()` | Verifica se uma condição é verdadeira. |
| `assertFalse()` | Verifica se uma condição é falsa. |
| `assertNull()` | Verifica se um valor é nulo. |
| `assertNotNull()` | Verifica se um valor não é nulo. |
| `assertThrows()` | Verifica se uma operação lança determinada exceção. |
| `assertAll()` | Executa várias verificações relacionadas. |

Exemplo:

```java
@Test
void shouldReturnTrue() {
    boolean result = true;

    assertTrue(result);
}
```

### Mockito

Mockito é uma biblioteca utilizada para criar objetos simulados, chamados de *mocks*.

Esses objetos substituem dependências reais durante os testes, permitindo controlar seus comportamentos.

Por exemplo, ao testar um `AuthService`, podemos simular o comportamento de:

- UserRepository;
- ProfileService;
- PasswordEncoder;
- TokenService.

Assim, não precisamos acessar um banco de dados ou executar serviços externos para testar a lógica do serviço.

Principais recursos:

| Recurso | Funcionalidade |
|---|---|
| `@Mock` | Cria um objeto simulado. |
| `@InjectMocks` | Cria a classe testada e injeta os mocks nela. |
| `when().thenReturn()` | Define o retorno de uma chamada simulada. |
| `when().thenThrow()` | Define uma exceção para uma chamada simulada. |
| `verify()` | Verifica se determinado método foi chamado. |
| `never()` | Verifica se um método nunca foi chamado. |
| `times()` | Verifica quantas vezes um método foi chamado. |
| `any()` | Aceita qualquer argumento compatível com o tipo informado. |
| `eq()` | Verifica um argumento com valor específico. |
| `ArgumentCaptor` | Captura argumentos utilizados em chamadas de mocks. |

---

## 3. Estrutura de um teste: Arrange, Act e Assert

Uma das convenções mais utilizadas para organizar testes é o padrão AAA.

Ele divide o teste em três etapas:

1. **Arrange:** preparação dos dados e dependências.
2. **Act:** execução do método que está sendo testado.
3. **Assert:** verificação dos resultados.

Exemplo:

```java
@Test
void shouldReturnTrueWhenConditionIsValid() {

    // Arrange
    boolean expected = true;

    // Act
    boolean result = expected;

    // Assert
    assertTrue(result);
}
```

Em testes de serviços, essa organização fica ainda mais importante, pois normalmente precisamos configurar mocks antes de executar o método.

### Arrange

Preparamos os dados necessários e configuramos o comportamento das dependências.

### Act

Executamos o método que queremos testar.

### Assert

Verificamos se o resultado e os efeitos produzidos correspondem ao esperado.

Um teste bem organizado deve permitir identificar facilmente essas três etapas.

---

## 4. Criando uma classe de teste

Imagine que temos um serviço chamado `AuthService`, responsável pelo cadastro de usuários.

Sua estrutura pode ser semelhante a esta:

```java
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final ProfileService profileService;

    public AuthService(
            UserRepository userRepository,
            ProfileService profileService
    ) {
        this.userRepository = userRepository;
        this.profileService = profileService;
    }

}
```

Para testar esse serviço isoladamente, podemos utilizar Mockito.

```java
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProfileService profileService;

    @InjectMocks
    private AuthService authService;

}
```

### Entendendo as anotações

**@ExtendWith(MockitoExtension.class)**

Integra o Mockito ao JUnit 5, permitindo inicializar automaticamente os mocks durante os testes.

**@Mock**

Cria uma implementação simulada da dependência.

Por exemplo:

```java
@Mock
private UserRepository userRepository;
```

Não estamos criando um repositório real. Estamos criando um objeto controlado pelo Mockito.

**@InjectMocks**

Cria a instância da classe que queremos testar e injeta nela os mocks correspondentes.

Neste exemplo, o Mockito cria o `AuthService` utilizando os mocks de `UserRepository` e `ProfileService`.

Isso permite testar a lógica do serviço sem precisar iniciar todo o contexto do Spring.

---

## 5. Simulando comportamentos com when e thenReturn

Uma das funcionalidades mais importantes do Mockito é configurar o retorno de métodos simulados.

Imagine que nosso repositório possui:

```java
boolean existsByEmail(String email);
```

Queremos simular uma situação em que o e-mail já existe.

```java
when(userRepository.existsByEmail("teste@email.com"))
        .thenReturn(true);
```

A leitura é:

- `when()`: quando determinada operação acontecer;
- `userRepository.existsByEmail()`: a consulta do e-mail for executada;
- `thenReturn(true)`: devolva `true`.

Ou seja, estamos instruindo o Mockito a responder que o e-mail já está cadastrado.

Não existe consulta real ao banco de dados.

### Simulando diferentes retornos

```java
when(userRepository.existsByEmail("teste@email.com"))
        .thenReturn(true);
```

Retorna `true`.

```java
when(userRepository.existsByEmail("novo@email.com"))
        .thenReturn(false);
```

Retorna `false`.

### Simulando exceções

Também podemos simular uma falha em uma dependência:

```java
when(userRepository.findById(1L))
        .thenThrow(new RuntimeException("Erro no banco"));
```

Quando o método for chamado, o Mockito lançará a exceção configurada.

Isso é útil para testar como nossa aplicação reage a falhas externas.

### Valores padrão dos mocks

Quando não configuramos o retorno de um mock, o Mockito utiliza valores padrão.

Exemplos:

| Tipo | Retorno padrão |
|---|---|
| `boolean` | `false` |
| `int` | `0` |
| `long` | `0L` |
| Objetos | `null` |
| Listas e outras coleções | Normalmente `null` |

Por isso, é importante configurar explicitamente os retornos necessários para cada cenário.

---

## 6. Testando exceções com assertThrows

O `assertThrows()` verifica se determinada operação lança uma exceção.

Sua estrutura é:

```java
assertThrows(
        TipoDaExcecao.class,
        () -> metodoQueDeveLancarExcecao()
);
```

Exemplo:

```java
@Test
void shouldThrowExceptionWhenAgeIsInvalid() {

    IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> service.validateAge(-1)
    );

}
```

O primeiro argumento define qual exceção esperamos.

O segundo argumento é uma expressão lambda que representa a operação que deve ser executada.

A lambda permite passar a chamada do método sem executá-la antecipadamente.

Se a exceção esperada for lançada, o teste continua.

Se nenhuma exceção acontecer, o teste falha.

Se outra exceção for lançada, também falha.

### Verificando a mensagem

Podemos guardar a exceção retornada e verificar sua mensagem:

```java
assertEquals(
        "Idade inválida",
        exception.getMessage()
);
```

### Verificando o status HTTP

Em aplicações Spring, podemos testar `ResponseStatusException`:

```java
ResponseStatusException exception = assertThrows(
        ResponseStatusException.class,
        () -> authService.register(request)
);

assertEquals(409, exception.getStatusCode().value());
```

Nesse caso, garantimos que o serviço lançou a exceção esperada e que ela representa o status HTTP 409.

---

## 7. Verificando chamadas com verify

O `verify()` permite verificar se determinado método de um mock foi executado.

Isso é especialmente importante quando queremos garantir que uma ação aconteceu ou deixou de acontecer.

Exemplo:

```java
verify(userRepository).save(any(User.class));
```

Verifica se o método `save()` foi chamado uma vez.

### Verificando que um método nunca foi chamado

```java
verify(userRepository, never()).save(any(User.class));
```

Garante que nenhum usuário foi salvo.

### Verificando quantidade de chamadas

```java
verify(userRepository, times(2))
        .existsByEmail(anyString());
```

Garante que o método foi chamado exatamente duas vezes.

### O que significa any()?

```java
any(User.class)
```

Significa que aceitamos qualquer argumento compatível com a classe `User`.

Isso não cria um usuário. Apenas define o argumento que será aceito durante a verificação.

### Outros exemplos

```java
verify(profileService).createUserProfile(any(User.class));
```

Verifica se o perfil foi criado uma vez.

```java
verify(userRepository, never()).delete(any(User.class));
```

Verifica se nenhum usuário foi excluído.

```java
verify(tokenService, times(1)).generateToken(any(User.class));
```

Verifica se o token foi gerado uma vez.

**Boa prática:** utilize `verify()` para verificar interações que realmente fazem parte da regra de negócio. Evite verificar cada chamada interna sem necessidade.

---

## 8. Exemplo prático: testando cadastro com e-mail duplicado

Imagine que nosso método de cadastro possui a seguinte lógica:

```java
public TokenResponse register(UserRequest request) {

    String email = normalizeEmail(request.email());

    if (userRepository.existsByEmail(email)) {
        throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "E-mail já cadastrado"
        );
    }

    // Continua o cadastro...
}
```

Nossa regra de negócio determina que não podemos cadastrar dois usuários com o mesmo e-mail.

O teste precisa garantir três coisas:

1. Uma exceção deve ser lançada.
2. O status HTTP deve ser 409.
3. O usuário e seu perfil não devem ser criados.

### Teste completo

```java
@Test
void shouldThrowExceptionWhenEmailAlreadyExists() {

    // Arrange
    String email = "teste@email.com";

    UserRequest request = new UserRequest(
            "Filipe",
            email,
            "Senha123!"
    );

    when(userRepository.existsByEmail(email))
            .thenReturn(true);

    // Act
    ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> authService.register(request)
    );

    // Assert
    assertEquals(409, exception.getStatusCode().value());

    verify(userRepository, never()).save(any(User.class));

    verify(profileService, never())
            .createUserProfile(any(User.class));
}
```

*Observação: os campos e a ordem do construtor de `UserRequest` devem corresponder ao DTO real do projeto.*

### Entendendo o motivo de criar o request e configurar o e-mail separadamente

São duas coisas diferentes:

**1. Criar o UserRequest**

```java
UserRequest request = new UserRequest(
        "Filipe",
        email,
        "Senha123!"
);
```

Representa os dados que seriam enviados pelo usuário para o cadastro.

**2. Configurar o retorno do repositório**

```java
when(userRepository.existsByEmail(email))
        .thenReturn(true);
```

Simula a resposta que o banco de dados daria quando o serviço consultasse aquele e-mail.

O serviço recebe o `request`, extrai o e-mail e consulta o repositório.

O Mockito está preparado para responder que aquele e-mail já existe.

Podemos reutilizar a variável `email` para evitar repetir a mesma String, mas ainda precisamos do `UserRequest`, pois é ele que será passado ao método `register()`.

---

## 9. Testando o fluxo de sucesso

Além dos cenários de erro, precisamos verificar se a operação funciona quando os dados são válidos.

No cadastro, devemos verificar:

- O e-mail ainda não existe.
- O usuário é criado.
- A senha é criptografada.
- O usuário é salvo.
- O perfil é criado, caso faça parte do fluxo.
- O token é gerado, caso faça parte do fluxo.
- A resposta é retornada corretamente.

Exemplo de estrutura:

```java
@Test
void shouldRegisterUserWhenEmailDoesNotExist() {

    // Arrange
    String email = "teste@email.com";

    UserRequest request = new UserRequest(
            "Filipe",
            email,
            "Senha123!"
    );

    when(userRepository.existsByEmail(email))
            .thenReturn(false);

    // Configurar os demais mocks necessários.

    // Act
    TokenResponse response = authService.register(request);

    // Assert
    assertNotNull(response);

    verify(userRepository).save(any(User.class));

    verify(profileService)
            .createUserProfile(any(User.class));
}
```

Esse exemplo é um esqueleto. Os demais mocks devem ser configurados conforme a implementação real do serviço.

### Atenção à senha

Um teste importante é garantir que a senha armazenada seja o resultado do `PasswordEncoder`, e não a senha original.

Por exemplo, podemos simular:

```java
when(passwordEncoder.encode("Senha123!"))
        .thenReturn("hash-da-senha");
```

Depois, utilizando um `ArgumentCaptor`, podemos verificar qual senha foi enviada ao repositório.

---

## 10. Capturando argumentos com ArgumentCaptor

O `ArgumentCaptor` permite capturar um argumento passado para um método de um mock.

É muito útil quando queremos verificar o conteúdo de um objeto que foi enviado para o repositório.

Exemplo:

```java
@Test
void shouldSaveUserWithEncodedPassword() {

    // Arrange
    String rawPassword = "Senha123!";
    String encodedPassword = "hash-da-senha";

    when(passwordEncoder.encode(rawPassword))
            .thenReturn(encodedPassword);

    // Configure os demais comportamentos necessários.

    // Act
    authService.register(request);

    // Assert
    ArgumentCaptor<User> captor =
            ArgumentCaptor.forClass(User.class);

    verify(userRepository).save(captor.capture());

    User savedUser = captor.getValue();

    assertEquals(encodedPassword, savedUser.getPassword());
}
```

### Como funciona?

**1. Criamos o capturador:**

```java
ArgumentCaptor<User> captor =
        ArgumentCaptor.forClass(User.class);
```

**2. Capturamos o argumento passado ao save:**

```java
verify(userRepository).save(captor.capture());
```

**3. Recuperamos o objeto capturado:**

```java
User savedUser = captor.getValue();
```

**4. Verificamos suas propriedades:**

```java
assertEquals(encodedPassword, savedUser.getPassword());
```

Assim, verificamos o conteúdo real do objeto enviado ao repositório, sem precisar consultar o banco.

---

## 11. Organizando dados de teste

Quando vários testes utilizam os mesmos dados, podemos criar métodos auxiliares.

Exemplo:

```java
private UserRequest validUserRequest() {
    return new UserRequest(
            "Filipe",
            "teste@email.com",
            "Senha123!"
    );
}
```

Assim, podemos reutilizar:

```java
UserRequest request = validUserRequest();
```

### Utilizando @BeforeEach

Quando vários testes precisam da mesma preparação, podemos utilizar:

```java
@BeforeEach
void setUp() {
    // Preparação comum aos testes.
}
```

Esse método é executado antes de cada teste.

Entretanto, não coloque toda a preparação nele automaticamente.

Dados e configurações específicos de cada cenário devem permanecer no próprio teste, para que sua intenção fique clara.

---

## 12. Testes unitários e testes de integração

Existem diferentes níveis de testes.

### Testes unitários

Testam uma unidade específica da aplicação, geralmente uma classe ou método.

Características:

- Execução rápida.
- Dependências externas normalmente simuladas.
- Não precisam iniciar o contexto completo do Spring.
- Foco nas regras de negócio.

Exemplo:

```java
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    // Testes do serviço.
}
```

### Testes de integração

Verificam se diferentes componentes funcionam corretamente em conjunto.

Podem envolver:

- Spring Context.
- Banco de dados.
- Repositórios.
- Serviços.
- Configurações.
- Integrações externas.

Uma anotação comum:

```java
@SpringBootTest
class ApplicationIntegrationTest {
}
```

Ela carrega o contexto da aplicação e pode tornar os testes mais lentos.

### Testes de repositório

Para testar consultas e mapeamentos JPA, podemos utilizar:

```java
@DataJpaTest
class UserRepositoryTest {
}
```

Esse tipo de teste prepara um contexto focado na camada de persistência.

Para testar comportamentos específicos do MySQL, prefira um banco de teste compatível, por exemplo utilizando Testcontainers.

### Testes de Controller

Para testar endpoints HTTP sem iniciar um servidor completo, podemos utilizar:

```java
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
}
```

O `MockMvc` permite simular requisições HTTP e verificar:

- Status HTTP.
- Corpo da resposta.
- Headers.
- Validações.
- Comportamento do controller.

**Importante:** não utilize `@SpringBootTest` para todos os testes. Escolha o tipo de teste de acordo com o comportamento que deseja verificar.

---

## 13. Boas práticas profissionais

### 1. Utilize nomes descritivos

Um nome de teste deve explicar o comportamento esperado.

Exemplos:

```java
shouldThrowExceptionWhenEmailAlreadyExists()
shouldRegisterUserWhenRequestIsValid()
shouldNotSaveUserWhenEmailAlreadyExists()
shouldReturnConflictWhenEmailIsAlreadyRegistered()
```

Também podemos utilizar `@DisplayName`:

```java
@Test
@DisplayName("Deve impedir o cadastro quando o e-mail já existe")
void shouldRejectRegistrationWhenEmailAlreadyExists() {
}
```

### 2. Teste comportamentos, não detalhes internos

Prefira verificar se uma regra de negócio foi respeitada em vez de testar cada linha da implementação.

### 3. Evite testes dependentes entre si

Cada teste deve preparar seus próprios dados e funcionar independentemente dos outros.

### 4. Não utilize o banco de desenvolvimento

Testes que precisam de banco devem utilizar uma base isolada e controlada.

### 5. Não exagere nos mocks

Utilize mocks somente quando houver necessidade de controlar uma dependência ou verificar uma interação importante.

### 6. Teste cenários de sucesso e falha

Não teste somente o caminho feliz. As regras de negócio precisam ser protegidas também nos cenários de erro.

### 7. Evite verificações desnecessárias

Muitos `verify()` podem deixar os testes frágeis e dificultar refatorações.

### 8. Utilize dados claros

Prefira variáveis com nomes descritivos em vez de espalhar valores sem contexto.

### 9. Verifique propriedades importantes

Um `assertNotNull()` sozinho não garante que o objeto esteja correto. Verifique também os valores relevantes.

### 10. Garanta que o teste realmente detecte erros

Um teste útil deve falhar caso o comportamento esperado deixe de funcionar.

---

## 14. Executando os testes com Maven

Na raiz do projeto, podemos executar:

Windows:

```powershell
.\mvnw.cmd test
```

Linux/macOS:

```bash
./mvnw test
```

Para executar somente uma classe:

```powershell
.\mvnw.cmd -Dtest=AuthServiceTest test
```

Se não estiver utilizando Maven Wrapper:

```bash
mvn test
```

### Quando um teste falhar

1. Leia o nome do teste que falhou.
2. Verifique a mensagem de erro.
3. Identifique qual asserção ou chamada apresentou problema.
4. Confira os dados de entrada.
5. Confira os retornos configurados nos mocks.
6. Analise se a implementação respeita a regra esperada.
7. Corrija o problema e execute novamente.

Não altere uma asserção apenas para fazer o teste passar. Primeiro entenda por que ele falhou.

---

## 15. Estratégia para criar testes

Antes de escrever um teste, responda:

1. Qual comportamento quero garantir?
2. Quais dados preciso fornecer?
3. Quais dependências precisam ser simuladas?
4. Qual resultado espero?
5. Quais ações não devem acontecer?

Depois, siga este processo:

1. Prepare os dados.
2. Configure os mocks.
3. Execute o método.
4. Verifique o resultado.
5. Verifique os efeitos colaterais importantes.
6. Execute o teste.
7. Corrija o código ou o teste conforme o problema identificado.

Uma abordagem interessante é o TDD (*Test-Driven Development*), em que escrevemos primeiro um teste que falha, implementamos a funcionalidade e depois ajustamos o código até o teste passar.

Não é necessário utilizar TDD em todas as situações, mas conhecer essa abordagem é útil.

---

## 16. Exercícios práticos

A melhor maneira de aprender testes é praticando em funcionalidades reais.

### Nível 1 — Fundamentos

- [ ] Criar a classe `AuthServiceTest`.
- [ ] Configurar `@ExtendWith(MockitoExtension.class)`.
- [ ] Criar os mocks das dependências.
- [ ] Criar uma requisição válida.
- [ ] Testar se e-mail duplicado lança uma exceção.
- [ ] Verificar se o status HTTP é 409.
- [ ] Verificar se o usuário não foi salvo.

### Nível 2 — Cadastro

- [ ] Testar o cadastro com dados válidos.
- [ ] Verificar se o usuário foi salvo.
- [ ] Verificar se o perfil foi criado.
- [ ] Verificar se a senha foi criptografada.
- [ ] Testar a normalização do e-mail.
- [ ] Testar falhas relevantes nas dependências.

### Nível 3 — Ecommerce

- [ ] Testar o cálculo do subtotal de um pedido.
- [ ] Testar o cálculo do desconto.
- [ ] Testar se não é possível comprar mais unidades do que o estoque disponível.
- [ ] Testar se um pedido não pode ser pago duas vezes.
- [ ] Testar se um usuário não pode acessar pedidos de outro usuário.
- [ ] Testar consultas personalizadas do repositório.
- [ ] Testar os endpoints e seus respectivos status HTTP.

### Nível 4 — Qualidade

- [ ] Garantir que os testes sejam independentes.
- [ ] Evitar mocks desnecessários.
- [ ] Testar os principais cenários de erro.
- [ ] Executar todos os testes pelo Maven.
- [ ] Revisar os nomes e a organização dos testes.

---

## 17. Checklist de um bom teste

Antes de considerar um teste concluído, verifique:

- [ ] O nome explica o comportamento esperado?
- [ ] O teste chama o método que realmente quero testar?
- [ ] Os dados de entrada estão claros?
- [ ] Os mocks simulam somente as dependências necessárias?
- [ ] O resultado esperado é verificado?
- [ ] Os efeitos colaterais importantes são verificados?
- [ ] O teste funciona independentemente dos outros?
- [ ] Não depende de dados externos ou do banco de desenvolvimento?
- [ ] Falharia se a regra de negócio fosse quebrada?
- [ ] Outra pessoa conseguiria entender facilmente o objetivo do teste?

---

## 18. Referências oficiais

- [JUnit 5 — User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito — Documentação oficial da API](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Spring Boot — Testing](https://docs.spring.io/spring-boot/reference/testing/index.html)
- [Spring Framework — MockMvc](https://docs.spring.io/spring-framework/reference/testing/mockmvc.html)

---

## Ordem recomendada de aprendizado

Para não tentar aprender tudo de uma vez, siga esta sequência:

1. `@Test` e asserções básicas.
2. `@Mock` e `@InjectMocks`.
3. `when()` e `thenReturn()`.
4. `assertThrows()`.
5. `verify()`, `never()` e `times()`.
6. `ArgumentCaptor`.
7. Testes de sucesso e falha em Services.
8. Testes de Repository.
9. Testes de Controller com MockMvc.
10. Testes de integração com Spring Boot.

**Objetivo final:** conseguir analisar uma regra de negócio, identificar seus cenários, preparar os dados, simular as dependências necessárias e escrever testes que garantam o comportamento esperado da aplicação.