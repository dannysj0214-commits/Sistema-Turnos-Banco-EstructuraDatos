# Sistema de Turnos - Banco Central - Trabajo colaborativo contextualizado - Estructura de datos.

**Asignatura:** Estructura de Datos  
**Programa:** Ingeniería de Software  
**Universidad:** Universidad de Cartagena (Programas a Distancia)  
**Integrantes:** Dany Jose Salgado Guzman, Raul Armando Valencia Zuluaga, Ronny Ricardo Romero Romero, Juan Andrés Ramos Almanza

---

##  Descripción del Proyecto
Este proyecto es un sistema de software desarrollado en **Java** que implementa una estructura de datos tipo **Cola (Queue)** para gestionar la fila de espera en un banco. El sistema resuelve la problemática de la atención desordenada, garantizando el principio **FIFO** (First In, First Out) y permitiendo la atención prioritaria para poblaciones vulnerables.

##  Características Principales
*   **Estructura Principal (Cola):** Uso de `LinkedList` implementando `Queue` para garantizar el orden de llegada.
*   **Estructura Secundaria (Lista):** Uso de `ArrayList` para almacenar el historial de atendidos.
*   **Prioridad:** Inserción al inicio de la fila para clientes preferenciales (`addFirst`).
*   **Interfaz Gráfica (GUI):** Desarrollada con **Java Swing**, con un diseño corporativo moderno, personalización de componentes (`paintComponent`) y simulación de tickets.
*   **Persistencia de Datos:** Guardado y carga automática de la fila en archivos de texto (`.txt`) mediante `BufferedReader` y `PrintWriter`.
*   **Validación:** Bloqueo de registros duplicados (búsqueda por cédula).
*   **Búsqueda Inteligente:** Filtrado en tiempo real (`DocumentListener`) en el historial de atendidos.

##  Estructura del Proyecto
El código está organizado bajo el paradigma de Programación Orientada a Objetos (POO):
*   `src/Cliente.java`: Clase modelo que representa a un usuario (nombre, cédula, trámite, prioridad, hora de llegada).
*   `src/GestorTurnos.java`: Clase controladora. Contiene la lógica de la Cola, la Lista, los métodos CRUD y la persistencia en archivos.
*   `src/InterfazTurnos.java`: Clase principal (Vista). Contiene la interfaz gráfica y los eventos de los botones.

##  Requisitos e Instalación
1. Tener instalado el **JDK de Java** (Versión 11 o superior).
2. Clonar este repositorio o descargar el archivo `.zip`.
3. Abrir la carpeta del proyecto en **Visual Studio Code** (o cualquier IDE como NetBeans, Eclipse o IntelliJ).
4. Ejecutar el archivo `InterfazTurnos.java`.

##  Conceptos de Estructuras de Datos Aplicados
*   **Complejidad Algorítmica:** Las operaciones de inserción (`offer`) y eliminación (`poll`) en la Cola tienen una complejidad de **O(1)** (tiempo constante).
*   **Persistencia:** Implementación de operaciones de Entrada/Salida (I/O) para evitar la pérdida de datos en la memoria RAM.
*   **FIFO (First In, First Out):** Aplicado para garantizar la justicia en la atención.

---
*Proyecto desarrollado para la entrega final del Trabajo Colaborativo Contextualizado (TCC).*