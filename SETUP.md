# FisioControl — Guia de Setup

## 1. Supabase

### 1.1 Criar projeto
1. Acesse [supabase.com](https://supabase.com) e crie um novo projeto
2. Anote o **Project URL** e a **anon public key** (Settings > API)

### 1.2 Executar o schema
1. No painel do Supabase, vá em **SQL Editor**
2. Cole e execute o conteúdo de `supabase_schema.sql`

### 1.3 Criar usuários da equipe
1. No painel, vá em **Authentication > Users**
2. Clique em **Invite user** e envie por e-mail
3. Ou clique em **Add user** para criar manualmente com e-mail e senha

---

## 2. Configurar credenciais no projeto

Edite o arquivo `local.properties` (nunca commite este arquivo):

```properties
SUPABASE_URL=https://seuproject.supabase.co
SUPABASE_ANON_KEY=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### Para iOS
Adicione as chaves no `Info.plist` do iosApp:
```xml
<key>SUPABASE_URL</key>
<string>https://seuproject.supabase.co</string>
<key>SUPABASE_ANON_KEY</key>
<string>eyJ...</string>
```

---

## 3. Android

1. Abra o projeto em Android Studio
2. Sincronize o Gradle (File > Sync Project with Gradle Files)
3. Selecione um emulador ou dispositivo físico
4. Run > Run 'composeApp'

---

## 4. iOS

1. Execute na raiz do projeto para gerar o framework:
   ```bash
   ./gradlew :composeApp:assembleXCFramework
   ```
2. Abra `iosApp/iosApp.xcodeproj` no Xcode
3. Adicione o framework gerado se necessário
4. Selecione simulador e pressione Run

> **Nota**: O projeto iOS requer macOS com Xcode 15+.

---

## 5. Web (Kotlin/Wasm)

1. Configure o `local.properties` como no passo 2 (SUPABASE_URL / SUPABASE_ANON_KEY)
   — o Gradle gera automaticamente `AppConfig.wasmJs.kt` a partir desses valores.
2. Rodar em modo desenvolvimento (hot-reload em `http://localhost:8080`):
   ```bash
   ./gradlew :composeApp:wasmJsBrowserDevelopmentRun
   ```
3. Gerar build de produção:
   ```bash
   ./gradlew :composeApp:wasmJsBrowserDistribution
   ```
   Saída em `composeApp/build/dist/wasmJs/productionExecutable/` — hospede em qualquer
   servidor estático (Netlify, Vercel, GitHub Pages, Supabase Storage, etc.)

> **Importante**: a versão Web **não possui cache local** (sem SQLDelight). Todas as
> operações dependem de conectividade direta com o Supabase; não há modo offline.

---

## 6. Arquitetura

```
App.kt
 ├─ LoginScreen          → autenticação com Supabase Auth
 └─ PlayerListScreen     → listagem e busca de atletas
      ├─ PlayerFormScreen    → cadastro/edição de atleta
      └─ PlayerDetailScreen  → detalhes do atleta
           ├─ ScheduleListScreen   → cronogramas de tratamento
           │   └─ ScheduleFormScreen  → cadastro/edição de cronograma
           └─ EvolutionListScreen  → histórico de evolução
               └─ EvolutionFormScreen → registro de sessão diária
```

### Camadas
- **presentation/** — Composables + ScreenModels (Voyager)
- **domain/** — modelos, interfaces de repositório, use cases
- **data/** — DTOs, datasources Supabase, implementações de repositório, cache SQLDelight (somente Android/iOS — Web opera 100% online contra o Supabase)
- **di/** — módulos Koin
