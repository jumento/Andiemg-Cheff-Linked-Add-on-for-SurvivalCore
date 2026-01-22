# SurvivalCore-Extended

A standalone add-on for Hytale that extends **SurvivalCore** functionality.

## Features
- **Thirst Mechanics**: Adds thirst restoration values to items.
- **Parallel Configuration**: Uses `SurvivalExtendedConfig.json` to manage stats without modifying SurvivalCore's files.
- **Soft Dependency**: Automatically disables if SurvivalCore is not initialized.

## Configuration Schema (`SurvivalExtendedConfig.json`)

The configuration uses a JSON Map where keys are Item Registry Names and values are an array of two floats.

### Structure
```json
{
  "ITEM_REGISTRY_NAME": [HUNGER_VALUE, THIRST_VALUE]
}
```

### Parameters
- **Key** (`String`): The exact registry name of the item (e.g., `Hytale_Apple`, `NoCube_Food_Roast_Bird`).
- **Value** (`Array<Float>`):
  - Index 0: **Hunger Restoration** (Float). Applied via standard hunger logic.
  - Index 1: **Thirst Restoration** (Float). Applied via extended thirst logic.

### Example
```json
{
  "NoCube_Food_Roast_Bird": [20.0, 5.0],
  "Hytale_Apple": [5.0, 1.0]
}
```

## Installation
1. Place the `SurvivalCore-Extended` jar in your server's `mods` folder.
2. Ensure `SurvivalCore` is also installed.
3. Start the server. The config file will be generated at `mods/SurvivalCore-Extended/SurvivalExtendedConfig.json`.
4. Edit the config to add custom values for items.
