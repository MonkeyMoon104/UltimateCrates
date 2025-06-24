<p align="center">
  <img src="https://img.shields.io/badge/Minecraft-1.21-blue?style=for-the-badge">
  <img src="https://img.shields.io/badge/API-Paper-yellow?style=for-the-badge">
  <img src="https://img.shields.io/badge/Version-1.1.8-brightgreen?style=for-the-badge">
  <img src="https://img.shields.io/badge/Proxy Support-BungeeCord%20%26%20Velocity-ff69b4?style=for-the-badge">
  <a href="https://github.com/MonkeyMoon104/UltimateCrates/releases/latest">
    <img src="https://img.shields.io/badge/Download-UltimateCrates-blueviolet?style=for-the-badge">
  </a>
  <a href="https://github.com/MonkeyMoon104/UltimateCrates/stargazers">
    <img src="https://img.shields.io/github/stars/MonkeyMoon104/UltimateCrates?style=for-the-badge&logo=github">
  </a>
</p>


<h1 align="center">UltimateCrates</h1>
<p align="center"><b>A powerful, animated and fully customizable crates plugin for Paper + Proxy Support (BungeeCord & Velocity) 1.21+</b></p>

---

## ✨ Main Features

* 🔑 **Physical & Virtual Keys**
* 📦 **Multiple Crate Types** with full customization
* 🎆 **Advanced Visual Effects & Animations**
* 📊 **Statistics & Top Players System**
* 🔌 **Official Developer API** to manage stats and leaderboards
* 🧠 **PlaceholderAPI Support** for integration in scoreboards, tabs, etc.
* 💰 **Vault Integration** for economy and key purchasing
* 🧼 **MySQL or SQLite Database Support**
* ♻️ **Reload Command** – no need to restart your server!
* 📅 **Events System** with 3 different type events

---

## 🌐 Language

* 🇮🇹 The default language is **Italian**
* ✍️ All texts can be translated by editing the `messages.yml` and `crates.yml` files
* 🧩 Full Unicode & color code support for multilingual servers

---

## ⚙️ Requirements

Make sure these dependencies are installed:

- [NBTAPI](https://www.spigotmc.org/resources/nbt-api.7939/)
- [PlayerParticles](https://www.spigotmc.org/resources/playerparticles.40261/)
- [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)
- [LuckPerms](https://luckperms.net/)
- [Vault](https://www.spigotmc.org/resources/vault.34315/)
- [WorldGuard](https://dev.bukkit.org/projects/worldguard)
- [WorldEdit](https://dev.bukkit.org/projects/worldedit)

---

## 🧩 Configuration Configproxy.yml

### `(messages proxy)`

```yaml
messages:
  no_permission: "&cNon hai il permesso per eseguire questo comando."
  player_not_found: "&cGiocatore non trovato."
  reload_success: "&aUCProxy ricaricato con successo."
  only_op: "&cSolo gli OP possono eseguire questo comando."
  server_not_found: "&cServer non trovato."
  server_textcomponent: "&a[Clicca per teletrasportarti al giocatore]"
  connect_failed: "&cConnessione al server fallita"
````

### `(template notify proxy)`

```yaml
event_proxy_notify:
  title: "&eNUOVO EVENTO INIZIATO"
  lines:
    - ''
    - '&7È iniziato un nuovo evento'
    - '&7Evento: &e%event%'
    - '&7Fine evento tra: &a%eventEnd% minuti'
    - '&7Server: &6%server%'
    - ''
````
---

## 🧩 Configuration Config.yml

### `(db function)`

```yaml
db_central:
  type: "sqlite"   # Scegli tra "sqlite" o "mysql" (se sqlite bypassa la configurazione host, port, etc.)
  host: "localhost"
  port: 3306
  database: "ultimatecrates"
  username: "root"
  password: "password"
````

### `(events function)`
```yaml
events:
  enabled: true #Funzione generale principale per eventi
  delay: 5 #durata degli eventi e durata di pausa tra un evento e l'altro (in minuti)
  world: "world" #Nome del mondo dove verranno eseguiti gli eventi
  random_select_event: #Su enabled mettere true altrimenti gli eventi non verranno avviati
    enabled: true
    selected_active_events: #Nome degli eventi presenti in list, tutti i nomi degli eventi che non sono presenti non verranno attivati e scelti
      - stats_hunt
  list:
    treasure_hunt:
      crate_id: "event" #ID della crate presente in crates.yml
      #player_animation: null #Non inserire l'animazione nell'evento treasure_hunt, questa animazione devi impostarla nel file crates.yml con la crate collegata
      pos: #Lista di posizioni "sicure" per far si che il plugin non mandi il server in sovraccarico cercando posizioni sicure, per posizioni sicure si intendono queste condizoni: "sopra un blocco con un area di spazio libera 3x3" (va bene anche sotto terra basta che rispetti le condizioni)
        - "1, 2, 3"
        - "4, 5, 6"
        - "7, 8, 9"

    key_hunt:
      key_name: "common" #ID della crate presente in crates.yml (otterrà automaticamente la chiave corrispondente)
      player_animation: "bubble_up" #Animazione che viene avviata sul player quando trova la chest evento
      open_animation: "multi_bolt" #Animazione che viene avviata sulla chest dell'evento quando viene reclamata
      amount: 2 #Numero di chiavi che l'evento dovrà dare al player vincitore

    stats_hunt:
      player_animation: "sparkle" #Animazione che viene avviata sul player quando trova la chest evento
      open_animation: "totem_explosion" #Animazione che viene avviata sulla chest dell'evento quando viene reclamata
      stats_increment: #Lista di ID delle crate per la quali le statistiche devono venire aumentate
        - "common"
        - "legendary"
      increment_amount: 2 #Quante statistiche per ciascuna crate devono venire aumentate
````

### `(proxy function)`
```yaml
proxy:
enabled: false #Abilita per ottenere le notifiche sincronizzate all'avvio di ogni evento, disabilita per non ricevere nulla (Questa funzione serve solo per le notifiche)
server-name: "servername" #Se abiliti il proxy assicurati di inserire il server-name corretto altrimenti verrà mandato il messaggio di notifica ma senza il server giusto infatti i player non potranno spostarsi nel server dove c'è l'evento!
````

### `crates.yml`

Each crate includes:

* Name, key type, customizable rewards
* Particles and animations (e.g. spiral, fireworks)
* Guaranteed reward after X openings (`reward.every`)
* Optional key purchasing support

Example:

```yaml
crates:
  common:
    name: "&aCommon Crate"
    keys:
      type: physic
      name: "&aCommon Crate Key"
      enchanted: true
      canbuy:
        enabled: true
        cost: 100.0
```

---

## 🔧 Commands - Proxy

| Command                                    | Description                   |
|--------------------------------------------|-------------------------------|
| `/crate`                                   | Main command                  |
| `/crate help`                              | Shows help                    |
| `/crate give <crate> <player>`             | Give a crate to a player      |
| `/crate givekey <crate> [player] [amount]` | Give physical or virtual keys |
| `/crate stats`                             | View or reset statistics      |
| `/crate virtualkeys`                       | Manage virtual keys           |
| `/crate top <crate>`                       | Show crate leaderboard        |
| `/crate buykey <crate> [amount]`           | Buy keys with Vault           |
| `/crate reload`                            | Reload plugin without restart |
| `/ucpreload` - `(Proxy)`                   | Reload Proxy Plugin           |
 

---

## 🔐 Permissions

### Admin - Proxy

* `uc.admin` – All administrative permissions

  * `uc.admin.give` - `bukkit`
  * `uc.admin.givekey` - `bukkit`
  * `uc.admin.reload` - `bukkit`
  * `uc.admin.stats.reset` - `bukkit`
  * `uc.admin.vkeys.reset` - `bukkit`
  * `uc.admin.break` - `bukkit`
  * `uc.admin.crate.place` - `bukkit`
  * `ucproxy.reload` - `PROXY`

### User - Proxy

* `uc.user` – All basic player permissions

  * `uc.help.use` - `bukkit`
  * `uc.stats.use` - `bukkit`
  * `uc.vkeys.use` - `bukkit`
  * `uc.crate.open` - `bukkit`
  * `uc.preview.see` - `bukkit`
  * `uc.top.use` - `bukkit`
  * `uc.buykey.use` - `bukkit`
  * `uc.crateevent.open` - `bukkit`
  * `ucproxy.event.notify` - `PROXY`

---

## 📅 Events System

UltimateCrates introduces an innovative **automated events system**, perfect for servers with active players!

### 🔄 Modalità disponibili:

| Event          | Description                                                               |
|----------------|---------------------------------------------------------------------------|
| `treasure_hunt` | Spawns an "event" crate that can be opened only once by a player          |
| `key_hunt`     | Spawns a chest that, when found, gives specific keys, openable only once by a player              |
| `stats_hunt`   | Spawns a chest that increases the player's stats on certain crates |

## 🎬 Small ShowCase Event
> ![Showcase](images/show1.gif)
> ![Showcase](images/show2.gif)

---

## 📢 API Interface!

UltimateCrates now includes an official API interface for developers!

[![UltimateCrates API](https://img.shields.io/badge/View%20on-GitHub-171515?style=for-the-badge&logo=github)](https://github.com/MonkeyMoon104/UCApi)

---

## 🔌 Developer API

UltimateCrates provides a clean and easy-to-use **developer API** via the `UltimateCratesProvider` interface.

### 🌎 JitPack Helper
[![](https://jitpack.io/v/MonkeyMoon104/UCApi.svg)](https://jitpack.io/#MonkeyMoon104/UCApi)


### 📦 Maven Dependency

```xml
<repository>
  <id>jitpack.io</id>
  <url>https://jitpack.io</url>
</repository>

<dependency>
  <groupId>com.github.MonkeyMoon104</groupId>
  <artifactId>UCApi</artifactId>
  <version>TAG</version>
</dependency>
```

### 🧩 Accessing the API

```java
import com.monkey.ultimatecrates.api.UltimateCratesAPI;
import com.monkey.ultimatecrates.api.UltimateCratesProvider;

UltimateCratesProvider api = UltimateCratesAPI.get();
```

### 🛠️ Available Methods

```java
public interface UltimateCratesProvider {

  void incrementCrateOpen(String playerName, String crateId, int amount);

  int getCrateOpens(String playerName, String crateId);

  List<LeaderboardEntry> getLeaderboard(String crateId, int page, int rowPerPage);

  void resetPlayerStats(String playerName);

  void resetAllStats();
}
```

### 🔍 API Status

```java
boolean available = UltimateCratesAPI.isRegistered();
```

### 📝 Registering the API (internal use only)

```java
public class MainPlugin extends JavaPlugin {
  public void onEnable() {
    UltimateCratesAPI.register(newProvider, getLogger());
  }
}
```

> ⚠️ The `register` method can only be called once. If it's called again, it will be ignored with a warning.

---

## 💬 Customizable Messages

All messages are editable from `messages.yml`. Supports color codes and dynamic placeholders like `%crate%`, `%amount%`, `%player%`, etc.

Example:

```yaml
command:
  unknown_command: "&cUnknown command. Use /crate help"
give:
  give_success: "&aYou gave crate '%crate%&a' to &b%player%."
```

---

## 🔮 Supported Effects & Animations

| Particle Effects   | Animations                |
| ------------------ | ------------------------- |
| heart, totem, etc. | spiral, fireworks, batman |

---

## 🏆 Stats & Leaderboards

Includes a powerful tracking system:

* Tracks openings per player & crate
* Global leaderboard per crate (`/crate top`)
* Guaranteed reward after X openings (e.g. every 5, 10...)

---

## 📸 Small Preview Example

> ![Crates](images/1.png)
> ![EffectCrates](images/2.png)
> ![StatsCrate](images/3.png)


---

## 🧪 Compatibility

* ✅ Fully tested on **Paper 1.21+**

---

## 👨‍💻 Author

**Made with ❤️ by MonkeyMoon104**

> Have ideas, suggestions or bugs to report?<br>
> [Open an issue](https://github.com/MonkeyMoon104/UltimateCrates/issues) or reach me on Discord!

---

## 🌟 Support the Project

<p align="center">
  <a href="https://github.com/MonkeyMoon104/UltimateCrates" target="_blank">
    <img src="https://img.shields.io/github/stars/MonkeyMoon104/UltimateCrates?style=for-the-badge&label=Leave%20a%20Star&logo=github" alt="GitHub Stars">
  </a>
  <a href="https://github.com/MonkeyMoon104/UltimateCrates/issues" target="_blank">
    <img src="https://img.shields.io/github/issues/MonkeyMoon104/UltimateCrates?style=for-the-badge&label=Report%20a%20Bug&logo=github" alt="GitHub Issues">
  </a>
</p>

<p align="center">
  <b>If you enjoy using UltimateCrates, please support it by leaving a ⭐ on GitHub.</b><br>
  ❤️‍🔥

</p>

---