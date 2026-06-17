# 🎨 Gestor Tintas — App Mobile (Android)

Aplicativo Android nativo do sistema **Gestor Tintas**, voltado ao Portal do Vendedor: login, consulta de estoque e preços, gestão de usuários e **monitoramento em tempo real da balança IoT** (pesagem da produção). Construído em **Kotlin + Jetpack Compose**, consome a API REST do backend.

> Parte do projeto **NPSV — Gestor Tintas**. Repositórios relacionados: BackEnd (Spring Boot), FrontEnd e IoT (firmware ESP32).

---

## 🧰 Tech Stack

| Camada | Tecnologia |
| :--- | :--- |
| Linguagem | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Arquitetura | MVVM (ViewModel + StateFlow) |
| Navegação | Navigation Compose |
| Rede | Retrofit 2 + OkHttp + Gson |
| Persistência local | DataStore Preferences (token JWT) |
| Imagens | Coil |
| Build | Gradle (Kotlin DSL) + Version Catalog |

---

## 📋 Pré-requisitos

- **Android Studio** (versão recente, recomendado Ladybug ou superior)
- **JDK 21** (o projeto usa toolchain 21; o `jbr-21` que vem com o Android Studio atende)
- **Android SDK** com:
  - `compileSdk 36`
  - `minSdk 24` (Android 7.0) — define os dispositivos suportados
  - `targetSdk 36`
- Um **emulador** (AVD) ou **dispositivo físico** com Android 7.0+
- **Backend acessível** — o app consome a API REST do Gestor Tintas (ver seção de configuração)

> As dependências são resolvidas automaticamente pelo Gradle (catálogo em `gradle/libs.versions.toml`). Não há instalação manual de bibliotecas.

---

## ⚙️ Configuração (URL da API)

O app **não usa arquivo `.env`** — o endereço da API é definido em código, na constante `BASE_URL` do arquivo:
app/src/main/java/com/senai/npsv_gestor_tintas_mobile/data/remote/RetroFit.kt

| Constante | Descrição | Valor atual |
| :--- | :--- | :--- |
| `BASE_URL` | URL base da API REST consumida pelo app | `https://gestor-tintas-backend-api.onrender.com` |

**Cenários comuns para ajustar a `BASE_URL`:**

| Cenário | Valor a usar |
| :--- | :--- |
| Backend em produção (Render) | `https://gestor-tintas-backend-api.onrender.com` |
| Backend local + **emulador** Android | `http://10.0.2.2:8080` (o `10.0.2.2` aponta para o `localhost` da máquina host) |
| Backend local + **dispositivo físico** | `http://<IP-da-sua-máquina-na-rede>:8080` |

> ⚠️ Para apontar a um backend local via **HTTP** (não HTTPS), o app já habilita `usesCleartextTraffic="true"` no `AndroidManifest.xml`. Em produção, prefira sempre HTTPS.

> 🔐 A autenticação é via **JWT**: após o login, o token é salvo no DataStore e injetado automaticamente no header `Authorization: Bearer <token>` pelo interceptor do OkHttp. Respostas `401` limpam o token e disparam expiração de sessão.

---

## 🚀 Passos de Execução

### Pela interface do Android Studio (recomendado)

```text
1. Clone o repositório:
   git clone <url-do-repositorio-mobile>

2. Abra a pasta no Android Studio (File > Open) e aguarde o Gradle sincronizar.

3. (Se necessário) Ajuste a BASE_URL em data/remote/RetroFit.kt
   conforme o backend que você quer consumir.

4. Selecione um emulador (AVD) ou conecte um dispositivo físico
   com depuração USB ativada.

5. Clique em "Run app" (▶) ou pressione Shift + F10.
```

### Pela linha de comando (Gradle Wrapper)

```bash
# Compilar o APK de debug
./gradlew assembleDebug          # Linux/macOS
gradlew.bat assembleDebug        # Windows

# Instalar em um dispositivo/emulador conectado
./gradlew installDebug

# Rodar os testes unitários
./gradlew test
```

O APK de debug é gerado em `app/build/outputs/apk/debug/`.

---

## 📲 Fluxo de Telas

| Tela | Função |
| :--- | :--- |
| **Login** | Autenticação via e-mail/senha → recebe e armazena o JWT |
| **Estoque** | Lista de produtos com busca, filtro por categoria e alerta de estoque baixo |
| **Preços** | Tabela de preços de venda com busca |
| **Usuários** | Listagem e cadastro de usuários (VENDEDOR, COLORISTA, ADMIN) |
| **Produção / Monitoramento** | Acompanhamento em tempo real da balança IoT (polling a cada 500ms no endpoint de pesagem) |

---

## 📂 Estrutura do Projeto

app/src/main/java/com/senai/npsv_gestor_tintas_mobile/

├── data/

│   ├── local/        # TokenStore (DataStore) e SessionManager

│   ├── remote/       # Retrofit, ApiService e DTOs

│   └── repository/   # Auth, Produto, Usuario, Monitoramento

├── di/appModule/     # Provedores de dependência (AppModule)

├── domain/model/     # Modelos de domínio (ex.: Produto)

└── ui/               # Telas Compose + ViewModels, por feature

├── login/  estoque/  precos/  usuarios/

├── prevendas/  producao/

├── navigation/   # AppNavGraph e Routes

└── theme/        # Cores, tipografia e tema

---

## 🎨 Paleta de Cores

Definida em `ui/theme/Color.kt`, alinhada à identidade visual do projeto:

| Cor | Hex | Uso |
| :--- | :--- | :--- |
| Primary | `#2E33FF` | Cor primária / destaque |
| Medium Blue | `#2B82FF` | Dados / conexão (tertiary) |
| Dark Blue | `#032055` | Estrutura |
| Warning Yellow | `#FFE72A` | Avisos |
| Success Green | `#2E7032` | Estoque adequado |
| Error Red | `#D32F2F` | Erros / estoque baixo |

---

## 🤝 Contribuição

Branches no padrão `tipo/descricao-da-tarefa`, commits no estilo Conventional Commits, nunca commitar direto na `main`, e abrir Pull Request com Assignees e Reviewer.
