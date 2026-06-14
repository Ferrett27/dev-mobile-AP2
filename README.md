# 🎮 Avaliador Proxy - Zenless Zone Zero (ZZZ)

* **Disciplina:** Desen
* **Professor:** Thiago Souza
* **Aluno:** Rodrigo de Sousa Ferrett


## 📖 Descrição da Proposta
O **Avaliador Proxy** é uma aplicação mobile nativa para Android que atua como um **sistema interativo de avaliação e classificação**. Ele permite a coleta de informações do inventário do usuário, realiza o processamento lógico desses dados (atributos, equipamentos e sub-status) e gera um **resultado personalizado** (Sync Rate variando de 0 a 100%, com Ranks S, A, B e C).

Todas as informações geradas e coletadas são armazenadas de forma persistente em um **banco de dados relacional** através da integração com uma **API REST** própria.

---

## 🛠️ Tecnologias Utilizadas

### 📱 Aplicativo (Frontend - Android)
- **Linguagem:** Kotlin.
- **Interface (UX/UI):** XML (ConstraintLayout, LinearLayout, ScrollView) com design focado no contraste (Dark Theme) e usabilidade.
- **Requisições HTTP:** `Retrofit2` integrado com `Coroutines` (lifecycleScope) para processamento assíncrono.
- **Imagens:** Biblioteca `Glide` para processamento de url de imagens no banco de dados.

### ⚙️ API (Backend)
- **Framework:** Spring Boot (Java).
- **Arquitetura:** RESTful trafegando dados em formato JSON.
- **Banco de Dados:** MySQL em nuvem (Clever Cloud)
- **Documentação:** Swagger / OpenAPI.
- **Api em nuvem:** Render
  - Link: https://dev-mobile-ap2.onrender.com/swagger-ui/index.html#/

---

## 🚀 Estrutura, Navegação e Requisitos Atendidos

Este projeto foi desenhado para cumprir as melhores práticas de desenvolvimento Mobile:

### 1. Telas e Fragments (Modularidade)
O aplicativo possui mais de 5 telas e utiliza uma arquitetura baseada em **2 Fragments principais**, garantindo alta performance na navegação:
* **PerfilFragment:** Responsável por carregar o inventário do usuário, suas builds e itens favoritados através de abas (TabLayout).
* **BuildFragment:** Responsável por listar o feed da comunidade.
* **Activities:** `LoginActivity`, `CadastroPersonagemActivity`, `DetalhesInventarioActivity`, `SelecaoBuildActivity`, e `ResultadoActivity`.

### 2. Navegação via Intents
* **Intents Explícitas:** Utilizadas para toda a navegação interna e passagem de parâmetros (ex: envio de IDs para a tela de edição ou deleção), implementando flags como `FLAG_ACTIVITY_CLEAR_TASK` no fluxo de Logout.
* **Intents Implícitas:** O aplicativo utiliza a ação `Intent.ACTION_SEND` na tela de resultados. Isso permite o **compartilhamento do resultado** alcançado pelo usuário para outros aplicativos do sistema (WhatsApp, X, Email, etc.), delegando a ação de envio ao Sistema Operacional.

### 3. Componentes Gráficos (Views)
A interface foi construída com consistência visual e uso de **mais de 6 componentes gráficos diferentes**, incluindo:
1. `TextView` (Textos e Títulos)
2. `EditText` (Campos de formulário e busca)
3. `Button` / `FloatingActionButton` (Ações principais)
4. `ImageView` (Avatares e ícones manipulados pelo Glide)
5. `Spinner` (Menus de seleção suspensos de equipamentos)
6. `RecyclerView` (Listagens dinâmicas de cards com `InventarioAdapter` e `BuildAdapter`)
7. `CardView` (Molduras das informações táticas)

---

## 🔗 Integração com API REST e Banco de Dados

O aplicativo se comunica com uma API desenvolvida em Spring Boot, realizando operações de CRUD completo (Create, Read, Update, Delete) do inventário do usuário. 
A persistência ocorre de forma estruturada em um **Banco de Dados Relacional**, onde o Backend lida com as relações de chaves estrangeiras (Usuários, Agentes, Armas e Discos).

* **Link da Documentação (Swagger):** `https://dev-mobile-ap2.onrender.com/swagger-ui/index.html#/`

---

## 📱 Prints / Screenshots do Aplicativo
| Tela de Login | Tela da Home | Tela de Perfil |
| :---: | :---: | :---: |
| <img width="1080" height="2340" alt="telaLogin" src="https://github.com/user-attachments/assets/57d015e3-0104-4422-8028-7cf0fd47f8f3" /> | <img width="1080" height="2340" alt="telaHome" src="https://github.com/user-attachments/assets/9ef8837f-6630-40e3-b061-73ebf106ba75" /> | <img width="1080" height="2340" alt="telaPerfil" src="https://github.com/user-attachments/assets/b123f4a5-b8e2-44b5-8467-b4aa0b49b4eb" /> |

| Cadastro de Equipamentos | Avaliação de Build (Rank) |
| :---: | :---: |
| <img width="1080" height="2340" alt="cadastroPersonagem" src="https://github.com/user-attachments/assets/452e3f3a-c65c-44f1-bb16-3b85e5b63048" /> | <img width="1080" height="2340" alt="avaliacaoPersonage," src="https://github.com/user-attachments/assets/15a840c3-5180-45d1-a8a3-89df8543ca85" /> |

---

## ⚙️ Instruções de Execução

1. **Configuração da API:**
   * Abra o link do swagger em nuvem e espere o render executar a aplicação da api
   * Após o swagger abrir poder ir para o android studio

1. **Configuração do Aplicativo Android:**
   * Clone este repositório e abra-o no **Android Studio**.
   * No Android Studio Abra na pasta ap2BuildsZzz.
   * Compile e execute o projeto em um Emulador ou Dispositivo Físico.
     * OBS: Se o app n estiver fazendo as requisições pode ser porque o render ficou inativo e desativou e tem que abrir de novo.
