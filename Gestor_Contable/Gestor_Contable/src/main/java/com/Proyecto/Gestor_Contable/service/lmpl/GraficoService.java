package com.Proyecto.Gestor_Contable.service.lmpl;
import com.Proyecto.Gestor_Contable.dtos.GraficoMensualResponse;
import com.Proyecto.Gestor_Contable.dtos.GraficoMensualResponse.MesGrafico;
import com.Proyecto.Gestor_Contable.dtos.GraficoMensualResponse.TotalesGrafico;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.*;

@Service
public class GraficoService {

    /**
     * Agrupa los movimientos de un negocio por mes (ingresos, egresos, gastos y utilidad)
     * para los diagramas de barras. Lee la coleccion directamente, asi no depende de las clases del modelo.*/

        private static final String[] MESES = {"Ene", "Feb", "Mar", "Abr", "May", "Jun",
                "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"};

        private final MongoTemplate mongoTemplate;

        public GraficoService(MongoTemplate mongoTemplate) {
            this.mongoTemplate = mongoTemplate;
        }

        public GraficoMensualResponse mensual(String negocioId, Integer anioPedido) {
            List<Document> docs = mongoTemplate.find(
                    new Query(Criteria.where("negocioId").is(negocioId)),
                    Document.class, "movimiento_financiero");

            // anio -> [mes][0 ingreso, 1 egreso, 2 gasto]
            Map<Integer, double[][]> porAnio = new TreeMap<>();

            for (Document d : docs) {
                Date fecha = d.get("fecha", Date.class);
                Number monto = d.get("monto", Number.class);
                Document tipo = d.get("tipoMovimiento", Document.class);
                if (fecha == null || monto == null || tipo == null || tipo.getString("nombre") == null) continue;

                int col = columna(tipo.getString("nombre"));
                if (col < 0) continue;

                Calendar c = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.systemDefault()));
                c.setTime(fecha);
                double[][] meses = porAnio.computeIfAbsent(c.get(Calendar.YEAR), k -> new double[12][3]);
                meses[c.get(Calendar.MONTH)][col] += monto.doubleValue();
            }

            List<Integer> anios = new ArrayList<>(porAnio.keySet());
            int anio = anioPedido != null ? anioPedido
                    : (anios.isEmpty() ? java.time.Year.now().getValue() : anios.get(anios.size() - 1));

            double[][] datos = porAnio.getOrDefault(anio, new double[12][3]);
            List<MesGrafico> lista = new ArrayList<>();
            double ti = 0, te = 0, tg = 0;
            for (int m = 0; m < 12; m++) {
                double i = r(datos[m][0]), e = r(datos[m][1]), g = r(datos[m][2]);
                lista.add(new MesGrafico(m + 1, MESES[m], i, e, g, r(i - e - g)));
                ti += i; te += e; tg += g;
            }
            return new GraficoMensualResponse(negocioId, anio, anios, lista,
                    new TotalesGrafico(r(ti), r(te), r(tg), r(ti - te - tg)));
        }

        private int columna(String nombre) {
            String n = nombre.trim().toUpperCase();
            if (n.startsWith("INGRES")) return 0;
            if (n.startsWith("EGRES")) return 1;
            if (n.startsWith("GAST")) return 2;
            return -1;
        }

        private double r(double v) {
            return Math.round(v * 100.0) / 100.0;
        }
}


