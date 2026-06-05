#!/usr/bin/env python3
"""Genera docs/analisis-patrones.pdf y docs/plan-branching.pdf para entrega académica."""

from pathlib import Path

try:
    from fpdf import FPDF
except ImportError:
    print("Instale fpdf2: pip install fpdf2")
    raise

DOCS = Path(__file__).parent


class PdfDoc(FPDF):
    def chapter_title(self, title):
        self.set_font("Helvetica", "B", 14)
        self.cell(0, 10, title, ln=True)
        self.ln(2)

    def body_text(self, text):
        self.set_font("Helvetica", "", 11)
        self.multi_cell(0, 6, text)
        self.ln(2)


def analisis_patrones():
    pdf = PdfDoc()
    pdf.add_page()
    pdf.set_font("Helvetica", "B", 16)
    pdf.cell(0, 10, "SMARTLOGIX - Analisis de Patrones y Arquitectura", ln=True)
    pdf.ln(5)

    sections = [
        ("Justificacion de microservicios", (
            "SMARTLOGIX separa inventario, pedidos y envios en servicios independientes "
            "con bases de datos propias. Esto permite escalar, desplegar y evolucionar cada "
            "dominio sin afectar al resto. La comunicacion es sincrona via REST entre servicios "
            "y el BFF expone una API unificada al frontend."
        )),
        ("Arquitectura BFF", (
            "El bff-gateway (puerto 8080) centraliza llamadas y oculta URLs internas. "
            "LogisticaFacade agrega pedido, productos y envio para el dashboard sin duplicar "
            "reglas de negocio complejas. El frontend solo consume /api del BFF."
        )),
        ("Repository Pattern", (
            "Problema: acoplar servicios a JPA/SQL. Solucion: interfaces ProductoRepository, "
            "PedidoRepository, EnvioRepository. Usado en los tres microservicios para abstraer persistencia."
        )),
        ("Builder Pattern", (
            "Problema: construccion de Producto con muchos campos y defaults. "
            "Solucion: ProductoBuilder en ms-inventario con metodo desdeRequest()."
        )),
        ("Factory Method", (
            "Problema: creacion de Pedido segun contexto (nuevo, rechazado). "
            "Solucion: PedidoFactory centraliza la instanciacion y estados iniciales."
        )),
        ("Facade Pattern", (
            "Problema: multiples llamadas REST desde pedidos a inventario y desde BFF a todos los MS. "
            "Solucion: InventarioFacade (ms-pedidos) y LogisticaFacade (bff-gateway) unifican operaciones."
        )),
    ]
    for title, body in sections:
        pdf.chapter_title(title)
        pdf.body_text(body)

    out = DOCS / "analisis-patrones.pdf"
    pdf.output(str(out))
    print(f"Generado: {out}")


def plan_branching():
    pdf = PdfDoc()
    pdf.add_page()
    pdf.set_font("Helvetica", "B", 16)
    pdf.cell(0, 10, "SMARTLOGIX - Plan de Branching (GitFlow)", ln=True)
    pdf.ln(5)

    content = [
        "Ramas: main (produccion), develop (integracion).",
        "Features: feature/inventario, feature/pedidos, feature/envios, feature/bff, feature/frontend.",
        "Flujo: branch desde develop -> commits -> Pull Request -> revision -> merge a develop.",
        "Release: merge develop a main con tag v1.0.0.",
        "Conflictos: rebase sobre develop, resolver archivos, mvn test, push y actualizar PR.",
        "Cada microservicio y el frontend pueden desarrollarse en paralelo en ramas aisladas.",
    ]
    for line in content:
        pdf.body_text(line)

    out = DOCS / "plan-branching.pdf"
    pdf.output(str(out))
    print(f"Generado: {out}")


if __name__ == "__main__":
    analisis_patrones()
    plan_branching()
