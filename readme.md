# NanoVault 🛡️⚡
> **Private, On-Device AI Notes & Executive Assistant for Android**

**NanoVault** is a privacy-first, 100% offline personal notes and executive assistant app for Android. It harnesses the system-level **Android AICore** service to run **Gemini Nano** directly on hardware-accelerated NPUs (such as Google Tensor) with zero cloud dependencies and zero data footprint outside your device.

The project demonstrates an enterprise-grade, modern Android architecture integrating on-device generative AI with local data persistence, security-at-rest, and seamless reactive streaming.

---

## ✨ Features

- **Zero-Cloud Generative AI:** Real-time text summarization, action-item extraction, and contextual proofreading executed locally on the device NPU via the AICore Prompt API.
- **Low-Latency Token Streaming:** Smooth typewriter-style rendering powered by Kotlin Coroutines `Flow` piped directly into Jetpack Compose without triggering unnecessary recompositions.
- **Hardware-Aware State Machine:** Robust verification pipeline monitoring NPU availability, background OS model weight downloads, and system thermal boundaries.
- **Security-First Persistence:** Fully encrypted database at rest using **SQLCipher** for Room, backed by cryptographically secure keys managed in the **Android KeyStore**.
- **Modern Clean Architecture:** Unidirectional Data Flow (UDF) adhering to MVI/MVVM design patterns, with dynamic type-safe routing and modular dependency injection.

---

## 🛠️ Tech Stack & Libraries

| Domain | Technologies & Libraries |
| :--- | :--- |
| **Language & Runtime** | Kotlin 2.4 (Coroutines, Flow, Serialization) |
| **UI Framework** | Jetpack Compose, Material 3, Compose-Markdown |
| **Navigation** | Navigation 3 (Type-Safe `@Serializable` routes) |
| **Dependency Injection**| Dagger Hilt |
| **On-Device AI** | Google AI Edge SDK, Android AICore (`gemini-nano`) |
| **Local Persistence** | Room Database, SQLCipher (encryption-at-rest), Proto DataStore |
| **Architecture** | Clean Architecture, Modular Structure, Unidirectional Data Flow (UDF) |

---

## 🏛️ Architecture Overview

The app follows Clean Architecture principles with clear separation between Domain, Data, Feature, and Hardware Engine layers:

```text
├── app/                  # Application initialization, root NavHost, Hilt setup
├── core/
│   ├── common/           # Dispatchers, Result wrappers, utility extensions
│   ├── database/         # Room Database, SQLCipher passphrase factory, DAOs, Entities
│   ├── datastore/        # Proto DataStore for typed configuration
│   ├── model/            # Pure domain models (State machines, Note domain objects)
│   └── ui/               # Design system, Material 3 theme, shared Compose components
├── data/
│   └── repository/       # Repository implementations bridging Room and AICore
├── engine/
│   └── aicore/           # Android AICore bridge, hardware readiness checks, Prompt API session
└── feature/
    ├── onboarding/       # Hardware capability verification & OS weight download status
    └── notes/            # Note list, editor, and real-time streaming AI action sheets