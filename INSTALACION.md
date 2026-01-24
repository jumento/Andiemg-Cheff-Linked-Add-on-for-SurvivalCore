# 🎮 Instalación del Mod - Andiemg Cheff UI

## ✅ Compilación Completada

**Archivo JAR generado:**

```
build/libs/SurvivalCore-Extended-1.0.4.jar
Tamaño: 19,860 bytes (~20 KB)
```

## 📦 Contenido del JAR

El JAR incluye:

- ✅ Comandos `/andiemgcheff` y `/cheff`
- ✅ Interfaz UI completa
- ✅ Archivo UI en: `Common/UI/Custom/Pages/AndiemgCheffConfig.ui`
- ✅ ConfigManager con métodos de guardado
- ✅ Todas las clases compiladas

## 🚀 Instalación en el Servidor

### Opción 1: Solo JAR (Recomendado)

El JAR ya contiene todo lo necesario. Simplemente:

```powershell
# Copiar el JAR al servidor
copy build\libs\SurvivalCore-Extended-1.0.4.jar [RUTA_SERVIDOR]\plugins\
```

### Opción 2: JAR + Archivos de Recursos (Por si acaso)

Si el servidor necesita los recursos por separado:

```powershell
# 1. Copiar el JAR
copy build\libs\SurvivalCore-Extended-1.0.4.jar [RUTA_SERVIDOR]\plugins\

# 2. Copiar recursos UI (opcional - ya está en el JAR)
xcopy /E /I src\main\resources\Common [RUTA_SERVIDOR]\Common\
```

### Ejemplo con ruta real

```powershell
# Asumiendo que el servidor está en C:\HytaleServer
copy build\libs\SurvivalCore-Extended-1.0.4.jar C:\HytaleServer\plugins\
```

## 🔄 Después de Copiar

1. **Reinicia el servidor de Hytale**
2. **Verifica en los logs** que el mod se cargó correctamente:

   ```
   [INFO] Registered commands: /andiemgcheff and /cheff
   ```

## 🎯 Probando el Mod

1. **Únete al servidor** como operador
2. **Ejecuta el comando:**

   ```
   /andiemgcheff
   ```

   o su alias:

   ```
   /cheff
   ```

3. **Deberías ver** la interfaz de configuración

## 🐛 Solución de Problemas

### Error: "Could not find document Common/UI/Custom/Pages/AndiemgCheffConfig.ui"

**Solución:**

1. Verifica que usaste el JAR correcto (19,860 bytes)
2. Si persiste, copia manualmente los recursos:

   ```powershell
   xcopy /E /I src\main\resources\Common [RUTA_SERVIDOR]\Common\
   ```

### El comando no funciona

**Verificar:**

- ✅ Eres operador del servidor
- ✅ El JAR está en la carpeta `plugins/`
- ✅ El servidor fue reiniciado después de copiar el JAR
- ✅ Revisar logs del servidor para mensajes de error

### El JAR no se carga

**Verificar:**

- ✅ Java version correcta (Java 21+)
- ✅ HytaleServer.jar está presente
- ✅ No hay conflictos con otros mods

## 📁 Estructura de Archivos

Después de la instalación, deberías tener:

```
[SERVIDOR]/
├── plugins/
│   └── SurvivalCore-Extended-1.0.4.jar
├── mods/
│   └── Andiemg Cheff Linked/
│       └── SurvivalExtendedConfig.json  (se crea automáticamente)
└── Common/  (opcional si el JAR no funciona solo)
    └── UI/
        └── Custom/
            └── Pages/
                └── AndiemgCheffConfig.ui
```

## ✨ Uso

Una vez instalado:

1. **Abrir UI:** `/andiemgcheff` o `/cheff`
2. **Seleccionar item** del dropdown
3. **Ingresar valores** de hunger y thirst
4. **Guardar cambios** con el botón verde
5. Los cambios se guardan en `SurvivalExtendedConfig.json`

## 📝 Notas Importantes

- ⚠️ **Solo operadores** pueden usar estos comandos
- ✅ Los cambios se guardan **inmediatamente** al presionar Save
- 🔄 **No requiere reinicio** del servidor para aplicar cambios
- 💾 El config se actualiza en `mods/Andiemg Cheff Linked/SurvivalExtendedConfig.json`

## 🎉 ¡Listo

El mod está completamente instalado y listo para usar. Los operadores ahora pueden modificar la configuración de hunger y thirst directamente desde el juego.
