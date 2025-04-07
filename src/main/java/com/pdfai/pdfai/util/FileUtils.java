package com.pdfai.pdfai.util;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class FileUtils {
    public String extractTextFromPDF(File pdfFile) throws IOException {
        try (PDDocument document = Loader.loadPDF(pdfFile)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }
    }
    public File getResultantFile(String text) throws IOException {
        File outputFile = new File(System.getProperty("java.io.tmpdir") + "/output.pdf");
        try (PDDocument document = new PDDocument()) {
            InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/NotoSans-Bold.ttf");
            if (fontStream == null) {
                throw new RuntimeException("Font file not found in resources!");
            }
            PDType0Font font = PDType0Font.load(document, fontStream);
            float fontSize = 12;
            float leading = fontSize;
            float margin = 50;
            PDRectangle pageSize = PDRectangle.A4;
            float pageWidth = pageSize.getWidth() - 8 * margin;
            float pageHeight = pageSize.getHeight() - margin;
            PDPage page = new PDPage(pageSize);
            document.addPage(page);
            text=text.replace(" ","\\u0020");
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(font, fontSize);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, pageHeight);
            float yPosition = pageHeight;
            String[] paragraphs = text.split("\n");
            for (String paragraph : paragraphs) {
                List<String> wrappedLines = wrapText(paragraph, font, fontSize, pageWidth);
                for (String line : wrappedLines) {
                    line=line.replace("\\u0020"," ");
                    contentStream.showText(line);
                    contentStream.newLineAtOffset(0, -leading);
                    yPosition -= leading;

                    if (yPosition <= margin) {
                        contentStream.endText();
                        contentStream.close();
                        page = new PDPage(pageSize);
                        document.addPage(page);
                        contentStream = new PDPageContentStream(document, page);
                        contentStream.setFont(font, fontSize);
                        contentStream.beginText();
                        yPosition = pageHeight;
                        contentStream.newLineAtOffset(margin, yPosition);
                    }
                }
                contentStream.newLineAtOffset(0, -leading);
                yPosition -= leading;
            }

            contentStream.endText();
            contentStream.close();
            document.save(outputFile);
            return outputFile;
        } catch (IOException e) {
            throw new RuntimeException("File processing failed", e);
        }
    }

    private List<String> wrapText(String text, PDType0Font font, float fontSize, float maxWidth) throws IOException {
        List<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();
        int spaceCount = 0;
        int maxSpacesPerLine = 30;

        text = text.replace("\\u0020", " ");

        for (char c : text.toCharArray()) {
            currentLine.append(c);
            if (c == ' ' && currentLine.length()>=70) {
                lines.add(currentLine.toString().replace(" ","\\u0020"));
                currentLine.setLength(0);
                spaceCount = 0;
            }

            if (c == ' ') {

                spaceCount++;
            }

            if (spaceCount >= maxSpacesPerLine) {
                lines.add(currentLine.toString().replace(" ","\\u0020"));
                currentLine.setLength(0);
                spaceCount = 0;
            }
        }
        if (!currentLine.isEmpty()) {
            lines.add(currentLine.toString().replace(" ","\\u0020"));
        }

        return lines;
    }

    public File highlightRed(File pdfFile, Set<String> highlightedSentences) throws IOException {
        File outputPdf = new File("highlighted_output.pdf");

        try (PDDocument document = Loader.loadPDF(pdfFile);
             PDDocument newDocument = new PDDocument()) {

            InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/NotoSans-Bold.ttf");
            if (fontStream == null) {
                throw new RuntimeException("Font file not found in resources!");
            }
            PDType0Font font = PDType0Font.load(newDocument, fontStream);
            PDFTextStripper stripper = new PDFTextStripper();
            int pageIndex = 1;
            for (PDPage page : document.getPages()) {
                PDPage newPage = new PDPage();
                newDocument.addPage(newPage);

                try (PDPageContentStream contentStream = new PDPageContentStream(newDocument, newPage, PDPageContentStream.AppendMode.APPEND, true, true)) {
                    contentStream.setFont(font, 12);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(50, 700);
                    stripper.setStartPage(pageIndex);
                    stripper.setEndPage(pageIndex);
                    String pageText = stripper.getText(document).replace("\n", "");
                    pageIndex++;  // Increment page index

                    String[] sentences = pageText.split("(?<=[.?!])\\s*");

                    for (String sentence : sentences) {
                        if (highlightedSentences.contains(sentence.trim())) {
                            contentStream.setNonStrokingColor(1.0f, 0.0f, 0.0f); // ✅ Red (highlighted)
                        } else {
                            contentStream.setNonStrokingColor(0.0f, 0.0f, 0.0f); // ✅ Black
                        }

                        String[] words = sentence.split(" ");
                        StringBuilder line = new StringBuilder();

                        for (String word : words) {
                            if (line.length() + word.length() > 80) { // ✅ Only break AFTER a word
                                contentStream.showText(line.toString().trim());
                                contentStream.newLineAtOffset(0, -15);
                                line.setLength(0); // ✅ Reset line buffer
                            }
                            line.append(word).append(" ");
                        }

                        if (!line.isEmpty()) { // ✅ Print last line if any words remain
                            contentStream.showText(line.toString().trim());
                            contentStream.newLineAtOffset(0, -15);
                        }
                    }

                    contentStream.endText();
                }
            }
            newDocument.save(outputPdf);
        }
        return outputPdf;
    }
}



