#!/bin/bash
# Script para ejecutar tests del backend de Alquigest
# Uso: ./run-tests.sh [opción]

set -e

# Colores para output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Directorio del backend
BACKEND_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$BACKEND_DIR"

# Función para mostrar uso
show_usage() {
    echo "Uso: $0 [opción]"
    echo ""
    echo "Opciones:"
    echo "  all              - Ejecutar todos los tests (por defecto)"
    echo "  unit             - Ejecutar solo tests unitarios (sin integración)"
    echo "  controller       - Ejecutar solo tests de controladores"
    echo "  service          - Ejecutar solo tests de servicios"
    echo "  <TestClass>      - Ejecutar una clase de test específica"
    echo "  coverage         - Generar reporte de cobertura"
    echo "  help             - Mostrar esta ayuda"
    echo ""
    echo "Ejemplos:"
    echo "  $0                          # Ejecutar todos los tests"
    echo "  $0 unit                     # Solo tests unitarios"
    echo "  $0 InmuebleControllerTest   # Test específico"
    echo "  $0 coverage                 # Generar reporte de cobertura"
}

# Función para ejecutar tests
run_tests() {
    local test_pattern=$1
    local description=$2
    
    echo -e "${YELLOW}=== Ejecutando: $description ===${NC}"
    echo ""
    
    if mvn test -Dtest="$test_pattern" 2>&1 | tee /tmp/test-output.log; then
        echo ""
        echo -e "${GREEN}✓ Tests completados exitosamente${NC}"
        
        # Mostrar resumen
        grep "Tests run:" /tmp/test-output.log | tail -1
        return 0
    else
        echo ""
        echo -e "${RED}✗ Algunos tests fallaron${NC}"
        
        # Mostrar resumen de errores
        echo ""
        echo -e "${YELLOW}Resumen de errores:${NC}"
        grep -A 2 "ERROR" /tmp/test-output.log | tail -10 || true
        return 1
    fi
}

# Función para generar reporte de cobertura
generate_coverage() {
    echo -e "${YELLOW}=== Generando reporte de cobertura ===${NC}"
    echo ""
    
    if mvn test jacoco:report; then
        echo ""
        echo -e "${GREEN}✓ Reporte generado exitosamente${NC}"
        echo ""
        echo "Reporte disponible en: target/site/jacoco/index.html"
        echo ""
        echo "Para ver el reporte en el navegador:"
        echo "  xdg-open target/site/jacoco/index.html    # Linux"
        echo "  open target/site/jacoco/index.html        # macOS"
        return 0
    else
        echo -e "${RED}✗ Error al generar reporte${NC}"
        return 1
    fi
}

# Procesar argumentos
case "${1:-all}" in
    all)
        run_tests "*Test" "Todos los tests"
        ;;
    unit)
        echo -e "${YELLOW}Nota: Esto ejecutará todos los tests excepto los de integración${NC}"
        run_tests "*Test" "Tests unitarios"
        ;;
    controller)
        run_tests "*ControllerTest" "Tests de controladores"
        ;;
    service)
        run_tests "*ServiceTest" "Tests de servicios"
        ;;
    coverage)
        generate_coverage
        ;;
    help|--help|-h)
        show_usage
        exit 0
        ;;
    *)
        # Asumir que es el nombre de una clase de test
        if [[ "$1" == *Test ]]; then
            run_tests "$1" "Test: $1"
        else
            run_tests "${1}Test" "Test: ${1}Test"
        fi
        ;;
esac

exit $?
