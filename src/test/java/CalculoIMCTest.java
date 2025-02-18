import net.jqwik.api.Property;
import net.jqwik.api.constraints.DoubleRange;
import org.INFNET.TP1.CalculoIMC;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import net.jqwik.api.*;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.*;

public class CalculoIMCTest {
    @Test
    public void TestaCalculoIMCValoresValidos() {
        double imc = CalculoIMC.calcularPeso(50.00, 1.80);
        String classificacaoImc = CalculoIMC.classificarIMC(imc);
        assertEquals(15.432098765432098, imc);
        assertEquals("Magreza grave", classificacaoImc);

        imc = CalculoIMC.calcularPeso(55.00, 1.80);
        classificacaoImc = CalculoIMC.classificarIMC(imc);
        assertEquals(16.975308641975307, imc);
        assertEquals("Magreza moderada", classificacaoImc);

        imc = CalculoIMC.calcularPeso(58.00, 1.80);
        classificacaoImc = CalculoIMC.classificarIMC(imc);
        assertEquals(17.901234567901234, imc);
        assertEquals("Magreza leve", classificacaoImc);

        imc = CalculoIMC.calcularPeso(65.00, 1.80);
        classificacaoImc = CalculoIMC.classificarIMC(imc);
        assertEquals(20.061728395061728, imc);
        assertEquals("Saudável", classificacaoImc);

        imc = CalculoIMC.calcularPeso(80.00, 1.75);
        classificacaoImc = CalculoIMC.classificarIMC(imc);
        assertEquals(26.122448979591837, imc);
        assertEquals("Sobrepeso", classificacaoImc);

        imc = CalculoIMC.calcularPeso(95.00, 1.75);
        classificacaoImc = CalculoIMC.classificarIMC(imc);
        assertEquals(31.020408163265305, imc);
        assertEquals("Obesidade Grau I", classificacaoImc);

        imc = CalculoIMC.calcularPeso(115.00, 1.75);
        classificacaoImc = CalculoIMC.classificarIMC(imc);
        assertEquals(37.55102040816327, imc);
        assertEquals("Obesidade Grau II", classificacaoImc);

        imc = CalculoIMC.calcularPeso(150.00, 1.60);
        classificacaoImc = CalculoIMC.classificarIMC(imc);
        assertEquals(58.593749999999986, imc);
        assertEquals("Obesidade Grau III", classificacaoImc);
    }

    @Test
    public void TestaCalculoIMCValoresInvalidosEExtremos() {
        assertEquals(33.564013840830455, CalculoIMC.calcularPeso('a', 1.70));
        assertThrows(IllegalArgumentException.class, () -> CalculoIMC.calcularPeso(-5.00, 1.70));
        assertThrows(IllegalArgumentException.class, () -> CalculoIMC.calcularPeso(60.00, -1.20));
        assertThrows(IllegalArgumentException.class, () -> CalculoIMC.calcularPeso(0.00, 0.00));
        assertThrows(IllegalArgumentException.class, () -> CalculoIMC.calcularPeso(0.00, 1.50));
        assertThrows(IllegalArgumentException.class, () -> CalculoIMC.calcularPeso(100.00, 0.00));
        assertThrows(IllegalArgumentException.class, () -> CalculoIMC.calcularPeso(0.00, 0.00));
        assertThrows(IllegalArgumentException.class, () -> CalculoIMC.calcularPeso(150.00, 3.00));
        assertThrows(IllegalArgumentException.class, () -> CalculoIMC.calcularPeso(500.00, 2.00));
    }

    @Property
    void TestaIMCNuncaDeveSerNegativo(
            @ForAll @DoubleRange(min = 35, max = 250) double peso,
            @ForAll @DoubleRange(min = 1.40, max = 2.20) double altura
    ) {
        double imc = CalculoIMC.calcularPeso(peso, altura);
        assertTrue(imc >= 0);
    }


    @Test
    void TestaIMCComValoresExtremos() {
        double peso = pesosExtremos().sample();
        double altura = alturasExtremas().sample();
        assertThrows(IllegalArgumentException.class, () -> CalculoIMC.calcularPeso(peso, altura));
    }

    @Provide
    Arbitrary<Double> alturasExtremas() {
        return Arbitraries.of(0.5, 3.0, 5.0, 9.0, 300000000.0, 0.0, 0.1, 9999.0, 99999999999999999.0, -5.1, 5.3, 39.5);  // Valores incomuns de altura
    }

    @Provide
    Arbitrary<Double> pesosExtremos() {
        return Arbitraries.of(10.0, 9.0, -5.0, 500.0, 5000000.0, 99999.0, 30000.0, 5999999.0, 333333.0, -55555.0, 5.0, 0.1);  // Valores incomuns de altura
    }

    @Property
    void TestaIMCComValoresAleatorios(@ForAll double peso, @ForAll double altura) {
        double imc = CalculoIMC.calcularPeso(peso, altura);
        assertTrue(imc >= 0);
    }

    @Test
    void TestaCalculoIMCComMock() {
        try (MockedStatic<CalculoIMC> mockedStatic = mockStatic(CalculoIMC.class)) {
            mockedStatic.when(() -> CalculoIMC.calcularPeso(70.0, 1.75)).thenReturn(22.86);

            double resultado = CalculoIMC.calcularPeso(70.0, 1.75);
            assertEquals(22.86, resultado, 0.01);
        }
    }

    @Example
    void TestaIMCComCasosEspecíficos() {
        double imc = CalculoIMC.calcularPeso(70.0, 1.75);
        assertTrue(imc >= 0);

        assertThrows(IllegalArgumentException.class, () -> CalculoIMC.calcularPeso(-70.0, 1.75));
    }
}
