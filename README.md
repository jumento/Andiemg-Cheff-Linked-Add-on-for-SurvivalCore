# SurvivalCore-Extended

A standalone add-on for Hytale that extends **SurvivalCore** functionality with an in-game configuration UI system.

## ✨ Features

### Core Mechanics

- **Thirst Mechanics**: Adds thirst restoration values to items
- **Parallel Configuration**: Uses `SurvivalExtendedConfig.json` to manage stats without modifying SurvivalCore's files
- **Soft Dependency**: Automatically disables if SurvivalCore is not initialized

### 🎮 Configuration UI System (v1.0.5+)

- **Interactive UI Editor**: Edit hunger and thirst values directly in-game
- **Configuration Viewer**: Display all current configuration values
- **Three Commands**:
  - `/andiemgcheff` - Opens the configuration editor
  - `/cheff` - Alias for `/andiemgcheff`
  - `/cheffinfo` - Displays current configuration values

### UI Features

- Clean, modern interface with validation
- Default values (0.0) for empty fields to prevent errors
- Automatic range validation (0-1000) with adjustment
- Real-time configuration updates
- No server restart required after changes

## 📋 Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/andiemgcheff` | Opens the configuration editor UI | Operator |
| `/cheff` | Short alias for `/andiemgcheff` | Operator |
| `/cheffinfo` | Displays current configuration values | Operator |

## 🎨 UI Screenshots

### Configuration Editor

Edit hunger and thirst values for food items with a user-friendly interface:

- Item Name input field
- Hunger Value input field
- Thirst Value input field
- Save and Cancel buttons
- Listed available items for reference

### Configuration Viewer

View all configured items with their current values in a formatted table.

## 📝 Configuration Schema

The configuration uses a JSON Map where keys are Item Registry Names and values are an array of two floats.

### Structure

```json
{
  "ITEM_REGISTRY_NAME": [HUNGER_VALUE, THIRST_VALUE]
}
```

### Parameters

- **Key** (`String`): The exact registry name of the item (e.g., `Andiechef_Food_Rollo`, `Andiechef_Food_Sake`)
- **Value** (`Array<Float>`):
  - Index 0: **Hunger Restoration** (Float, 0-1000)
  - Index 1: **Thirst Restoration** (Float, 0-1000)

### Example

```json
{
  "Andiechef_Food_Rollo": [20.0, 5.0],
  "Andiechef_Food_Item_Soya": [15.0, 3.0],
  "Andiechef_Food_Sake": [5.0, 10.0]
}
```

## 🚀 Installation

1. Download `SurvivalCore-Extended-1.0.5.jar` from the releases
2. Place the jar in your server's `plugins` folder
3. Ensure `SurvivalCore` (EasyHunger) is also installed
4. Start the server
5. The config file will be generated at `mods/Andiemg Cheff Linked/SurvivalExtendedConfig.json`
6. Use `/cheff` command in-game to edit configurations, or edit the JSON file directly

## 🛠️ Technical Details

### Architecture

- **ECS Pattern**: Follows Hytale's Entity Component System architecture
- **Event-Driven UI**: Uses `InteractiveCustomUIPage` for event handling
- **Asset Pack System**: Resources loaded automatically via `IncludesAssetPack` manifest flag

### UI Implementation

- Native Hytale UI language (`.ui` files)
- `UICommandBuilder` for UI construction
- `UIEventBuilder` for event binding
- `BuilderCodec` for event data serialization

### Code Structure

```
src/main/java/com/jume/andiemgchefflinked/
├── commands/
│   ├── AndiemgCheffCommand.java      # Main config editor command
│   ├── CheffCommand.java              # Alias command
│   └── CheffInfoCommand.java          # Config viewer command
├── ui/
│   ├── ConfigEditorPage.java         # Interactive editor UI
│   └── ConfigInfoPage.java           # Read-only info UI
├── config/
│   └── ConfigManager.java            # Configuration management
├── events/
│   └── ConsumptionListener.java      # Item consumption handler
└── SurvivalCoreExtended.java         # Main plugin class

src/main/resources/
├── Common/UI/Custom/Pages/
│   ├── AndiemgCheffConfig.ui         # Editor UI definition
│   └── AndiemgCheffInfo.ui           # Info UI definition
├── manifest.json                      # Plugin manifest
├── mod.json                           # Mod metadata
└── SurvivalExtendedConfig.json       # Default config
```

## 📦 Building from Source

```bash
gradle clean build
```

The jar will be generated in `build/libs/SurvivalCore-Extended-1.0.5.jar`

## 🔄 Version History

### v1.0.5 (Latest)

- ✅ Added interactive configuration UI system
- ✅ Three new commands: `/andiemgcheff`, `/cheff`, `/cheffinfo`
- ✅ ConfigEditorPage for in-game editing
- ✅ ConfigInfoPage for viewing current config
- ✅ Enhanced validation with default values
- ✅ Prevents UI hang with proper event handling
- ✅ Clean error messages without color codes
- ✅ Range validation (0-1000) with auto-adjustment

### v1.0.4

- Improved event system integration
- Enhanced configuration management

### v1.0.0

- Initial release
- Basic hunger and thirst mechanics
- Configuration file system

## 🤝 Credits

- **Authors**: jume, andiemg, Antigravity
- **For**: Hytale Server (ECS Architecture)
- **Requires**: SurvivalCore (EasyHunger mod)

## 📄 License

This project is for use with Hytale servers.

## 🐛 Troubleshooting

### UI doesn't open

- Ensure you have operator permissions
- Check server logs for errors
- Verify the plugin loaded successfully

### Changes not saving

- Check file permissions in `mods/Andiemg Cheff Linked/`
- Look for error messages in chat
- Verify the item name is correct

### UI freezes

- Update to v1.0.5 or later
- The bug was fixed with proper event handling

## 📞 Support

For issues or questions, check the server logs or contact the development team.
