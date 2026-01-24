# ⚠️ INSTRUCCIONES CRÍTICAS DE INSTALACIÓN

## 🔴 PROBLEMA ACTUAL

El servidor muestra este error:

```
Could not find document Pages/AndiemgCheffConfig.ui
```

Esto significa que **LA CARPETA Common/ NO ESTÁ EN EL SERVIDOR** o está en la ubicación incorrecta.

## ✅ SOLUCIÓN PASO A PASO

### PASO 1: Extraer el ZIP

Extrae `SurvivalCore-Extended-Deploy.zip` en tu PC.

Deberías ver:

```
📁 Carpeta extraída/
├── SurvivalCore-Extended-1.0.4.jar
└── Common/
    └── UI/
        └── Custom/
            └── Pages/
                └── AndiemgCheffConfig.ui
```

### PASO 2: Subir EL JAR

**Ubicación destino:** `/plugins/`

1. Abre tu panel FTP/SFTP o File Manager del hosting
2. Navega a la carpeta `plugins/`
3. Sube `SurvivalCore-Extended-1.0.4.jar`

### PASO 3: Subir LA CARPETA Common/ (ESTE ES EL PASO CRÍTICO)

**Ubicación destino:** RAÍZ DEL SERVIDOR (NO en plugins/)

1. En tu FTP/Panel, vuelve a la **RAÍZ del servidor**
   - La raíz es donde están las carpetas: `plugins/`, `mods/`, `worlds/`, etc.
2. Sube la carpeta **COMPLETA** llamada `Common/`
3. La estructura final debe ser:

```
[RAÍZ DEL SERVIDOR]/
├── plugins/
│   ├── SurvivalCore-Extended-1.0.4.jar  ✅ (El JAR)
│   └── [otros plugins...]
├── Common/                               ✅ (Esta carpeta DEBE existir)
│   └── UI/
│       └── Custom/
│           └── Pages/
│               └── AndiemgCheffConfig.ui  ✅ (Este archivo DEBE existir)
├── mods/
├── worlds/
└── [otros archivos del servidor...]
```

### PASO 4: Verificar la Ruta

**MUY IMPORTANTE:** Verifica que la ruta completa sea exactamente:

```
/Common/UI/Custom/Pages/AndiemgCheffConfig.ui
```

**NO debe ser:**

- ❌ `/plugins/Common/UI/Custom/Pages/AndiemgCheffConfig.ui`
- ❌ `/mods/Common/UI/Custom/Pages/AndiemgCheffConfig.ui`
- ❌ `/Common/AndiemgCheffConfig.ui`

### PASO 5: Reiniciar y Probar

1. Reinicia el servidor
2. Únete como operador
3. Ejecuta: `/andiemgcheff`
4. Si funciona, verás la interfaz ✅

## 🔍 CÓMO VERIFICAR SI ESTÁ CORRECTAMENTE INSTALADO

### En tu FTP/Panel de Archivos

1. Ve a la **raíz** del servidor
2. Debes ver una carpeta llamada `Common` al mismo nivel que `plugins`
3. Entra en: `Common` → `UI` → `Custom` → `Pages`
4. Debes ver el archivo: `AndiemgCheffConfig.ui`

### Checklist

- [ ] El JAR está en `/plugins/SurvivalCore-Extended-1.0.4.jar`
- [ ] La carpeta Common/ existe en la raíz del servidor
- [ ] El archivo existe en `/Common/UI/Custom/Pages/AndiemgCheffConfig.ui`
- [ ] El servidor fue reiniciado después de subir los archivos
- [ ] Tienes permisos de operador

## ❓ SI SIGUE SIN FUNCIONAR

### Error persiste después de subir Common/

1. **Verifica permisos de lectura** del archivo .ui
2. **Asegúrate** que la carpeta `Common` está en la RAÍZ, no dentro de `plugins`
3. **Revisa** que no exista otra carpeta `Common` que esté causando conflicto
4. **Comprueba** en los logs del servidor si hay otros errores

### Para proveedores específicos

**Nitrado:**

- La raíz es donde ves `plugins/`, `mods/`, `worlds/`
- Sube `Common/` ahí mismo

**Aternos:**

- Ve a Files → Raíz del servidor
- Sube `Common/` donde están las demás carpetas

**Servidor propio/VPS:**

- La raíz es donde ejecutas el `HytaleServer.jar`
- Por ejemplo: `/home/minecraft/server/`

## 📋 RESUMEN

1. ✅ Subir JAR a `/plugins/`
2. ✅ Subir carpeta `Common/` a la RAÍZ (no a plugins)
3. ✅ Verificar que `/Common/UI/Custom/Pages/AndiemgCheffConfig.ui` existe
4. ✅ Reiniciar servidor
5. ✅ Probar con `/andiemgcheff`

## 💡 NOTA IMPORTANTE

El archivo UI **NO puede estar dentro del JAR**. Hytale **REQUIERE** que los archivos `.ui` estén en el sistema de archivos del servidor en la carpeta `Common/`.

Esto es por diseño de Hytale, no es un bug.
