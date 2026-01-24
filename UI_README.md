# Andiemg Cheff Configuration UI

## Descripción

Este módulo agrega una interfaz de usuario (UI) para configurar los valores de hambre y sed de los items del mod Andiemg Cheff.

## Características

✅ **Comando `/andiemgcheff`** - Abre la interfaz de configuración
✅ **Alias `/cheff`** - Atajo para el comando principal  
✅ **Solo para Operadores** - Configurado para acceso administrativo
✅ **Interfaz Interactiva** - Edita valores directamente desde el juego
✅ **Guardado Automático** - Los cambios se guardan en `SurvivalExtendedConfig.json`

## Uso

### 1. Ejecutar el Comando

Como operador del servidor, ejecuta uno de estos comandos:

```
/andiemgcheff
```

o su alias:

```
/cheff
```

### 2. Usar la Interfaz

La interfaz te permitirá:

1. **Seleccionar un item** del dropdown
2. **Ingresar el valor de Hambre** (Hunger Value)
3. **Ingresar el valor de Sed** (Thirst Value)
4. **Guardar** los cambios o **Cancelar**

### 3. Items Disponibles

Los siguientes items están disponibles para configuración:

- `Andiechef_Food_Rollo`
- `Andiechef_Food_Item_Soya`
- `Andiechef_Food_Nigiri`
- `Andiechef_Food_Onigiri`
- `Andiechef_Food_Sake`
- `Andiechef_Ingredient_SalsaSoya`
- `Andiechef_Ingredient_Wasabi`
- `Andiechef_Item_Soya_Fermentada`
- `Andiechef_Food_BubbleTea`

## Estructura del Código

```
src/main/java/com/jume/andiemgchefflinked/
├── commands/
│   ├── AndiemgCheffCommand.java    # Comando principal
│   └── CheffCommand.java            # Alias del comando
├── ui/
│   └── ConfigEditorPage.java       # Lógica de la UI
├── config/
│   └── ConfigManager.java          # Gestión de configuración
└── SurvivalCoreExtended.java       # Clase principal del mod

src/main/resources/
└── Pages/
    └── AndiemgCheffConfig.ui       # Definición de la interfaz
```

## Arquitectura ECS

Este mod sigue la arquitectura **Entity Component System (ECS)** de Hytale:

- **Entities**: Representados por `EntityStore` y referencias
- **Components**: Como `Player.getComponentType()`
- **Systems**: Manejados por eventos y comandos

## Permisos

Para usar el comando, los jugadores deben ser **operadores** del servidor. El sistema de permisos se maneja a nivel del servidor de Hytale.

## Configuración

El archivo de configuración se encuentra en:
```
mods/Andiemg Cheff Linked/SurvivalExtendedConfig.json
```

Formato del archivo:
```json
{
  "ItemName": [hunger_value, thirst_value],
  "Andiechef_Food_Rollo": [20.0, 5.0],
  ...
}
```

## Compilación

Para compilar el mod:

```bash
gradle build
```

El JAR resultante estará en:
```
build/libs/SurvivalCore-Extended-1.0.4.jar
```

## Instalación

1. Compila el mod con `gradle build`
2. Copia el JAR a la carpeta `plugins` del servidor de Hytale
3. Copia la carpeta `Pages` a la ubicación correspondiente del servidor
4. Reinicia el servidor

## Notas Técnicas

- La UI utiliza el sistema de páginas personalizadas de Hytale
- Los eventos se manejan mediante `UIEventBuilder` y `EventData`
- Los valores se almacenan usando `BuilderCodec` para serialización
- La configuración se guarda automáticamente al hacer cambios

## Solución de Problemas

### El comando no funciona
- Verifica que seas operador del servidor
- Revisa los logs del servidor para errores

### La UI no se muestra
- Asegúrate que el archivo `.ui` esté en la ubicación correcta
- Verifica los permisos del archivo

### Los cambios no se guardan
- Revisa que tienes permisos de escritura en la carpeta `mods/`
- Comprueba los logs para errores de guardado

## Créditos

- **Desarrollado por**: Antigravity AI
- **Versión del Mod**: 1.0.4
- **Compatible con**: Hytale Server (ECS)
