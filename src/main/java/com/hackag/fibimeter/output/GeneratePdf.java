package com.hackag.fibimeter.output;

import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.datatable.DataTable;
import ch.qos.logback.classic.Level;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Steve on 26.10.2017.
 */
public class GeneratePdf {

    private static final Logger log = LoggerFactory.getLogger(GeneratePdf.class);

    private static final PDFont HEADER_FONT = PDType1Font.HELVETICA_BOLD;
    private static final PDFont CONTENT_FONT = PDType1Font.HELVETICA;

    public static void generateFeedbackReport(SummaryOfCompetenciesReport competenciesReport, String outputFileName) {
        log.trace("Started generation of PDF file " + outputFileName);
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            // Start a new content stream which will "hold" the to be created content
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Draw the content
                contentStreamHeader(document, contentStream, competenciesReport);
                contentStreamCompetencies(document, contentStream, competenciesReport);
            } catch (IOException e) {
                log.error("PDF content stream problem", e);
                throw new RuntimeException(e);
            }
            // Save the newly created document
            document.save(outputFileName);
            log.info("Saved PDF on disk to file " + new File(outputFileName).getAbsolutePath());
        } catch (IOException e) {
            log.error("PDF generation or storage problem", e);
            throw new RuntimeException(e);
        }
    }

    private static void contentStreamHeader(PDDocument document,
                                            PDPageContentStream contentStream,
                                            SummaryOfCompetenciesReport report) throws IOException {
        PDImageXObject balloon = PDImageXObject.createFromFile("src/main/resources/pdf/balloon.png", document);
        contentStream.drawImage(balloon, 18, 610);

        contentStream.beginText();
        contentStream.setFont(CONTENT_FONT, 13);
        contentStream.newLineAtOffset(210, 715);
        contentStream.showText("Feedback report for " + report.getPersonName());
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(HEADER_FONT, 16);
        contentStream.newLineAtOffset(210, 677);
        contentStream.setNonStrokingColor(Color.blue);
        contentStream.showText("Summary Of Competencies");
        contentStream.setNonStrokingColor(Color.black);
        contentStream.endText();
    }

    private static void contentStreamCompetencies(PDDocument document,
                                                  PDPageContentStream contentStream,
                                                  SummaryOfCompetenciesReport competenciesReport) throws IOException {
        PDPage page = document.getPage(0);
        float yStart = 588;
        float yStartNewPage = 785;
        float tableWidth = 590;
        BaseTable baseTable = new BaseTable(
            yStart, yStartNewPage, 0, tableWidth, 10, document, page, true, true);
        DataTable table = new DataTable(baseTable, page);
        java.util.List<java.util.List> data = new ArrayList<>();
        for (CompetencyReport competencyReport : competenciesReport.getCompetencyReports()) {
            data.add(Arrays.asList(
                competencyReport.getCompetencyName(),
                competencyReport.getCompetencyDescription(),
                competencyReport.getYourSelfScore(),
                competencyReport.getFeedbackScore()
            ));
        }
        table.addListToTable(data, false);
        baseTable.draw();
    }

    private static PDImageXObject loadTransparentPngImage(PDDocument document, File imageFile) throws IOException {
        BufferedImage tmpImage = ImageIO.read(imageFile);
        BufferedImage bufferedImage = new BufferedImage(tmpImage.getWidth(), tmpImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        bufferedImage.createGraphics().drawRenderedImage(tmpImage, null);
        return LosslessFactory.createFromImage(document, bufferedImage);
    }

    @FunctionalInterface
    private interface RunnableThrowsIOException {
        void run() throws IOException;
    }

    /**
     * Enables transparency with a specified alpha for any content displayed via function callback.
     */
    private static void enableTransparency(PDPageContentStream contentStream,
                                           float transparencyAlpha,
                                           RunnableThrowsIOException function) throws IOException {
        PDExtendedGraphicsState pdExtGfxState = new PDExtendedGraphicsState();
        pdExtGfxState.getCOSObject().setItem(COSName.BM, COSName.MULTIPLY);
        pdExtGfxState.setNonStrokingAlphaConstant(transparencyAlpha);
        contentStream.setGraphicsStateParameters(pdExtGfxState);
        function.run();
        contentStream.restoreGraphicsState();
    }

    public static void main(String[] args) {
        ((ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(
            ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME
        )).setLevel(Level.TRACE);
        // Example data
        java.util.List<CompetencyReport> competencies = new ArrayList<>(Arrays.asList(
            new CompetencyReport(
                "Team Working",
                "The ability to contribute to teams and to improve their effectiveness " +
                    "through personal commitment.",
                "3.33",
                "4.44"),
            new CompetencyReport(
                "Developing self",
                "The ability to focus on own development and to take action to learn.",
                "5.00",
                "2.15")
        ));
        for (int i = 0; i < 200; i++) {
            competencies.add(new CompetencyReport("Competency " + new Random().nextInt(9999),
                "The ability to do " + new Random().nextInt(99) + " things at once.",
                String.format("%.2f", (i * 6.41f) % 10f),
                "4.44"));
        }
        // Saving test PDF to project directory
        GeneratePdf.generateFeedbackReport(
            new SummaryOfCompetenciesReport("Homer Simpson", competencies), "pdfTest.pdf"
        );
    }
}
