package com.ecom.util;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.model.ProductOrder;
import com.ecom.model.UserDtls;
import com.ecom.service.UserService;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.net.URL;

import org.springframework.core.io.ByteArrayResource;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Component
public class CommonUtil {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserService userService;

    @Value("${aws.s3.bucket.category}")
    private String categoryBucket;

    @Value("${aws.s3.bucket.product}")
    private String productBucket;

    @Value("${aws.s3.bucket.profile}")
    private String profileBucket;

    public Boolean sendMail(String url, String reciepentEmail) throws UnsupportedEncodingException, MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("daspabitra55@gmail.com", "Shooping Cart");
        helper.setTo(reciepentEmail);

        String content = "<p>Hello,</p>" + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>" + "<p><a href=\"" + url
                + "\">Change my password</a></p>";
        helper.setSubject("Password Reset");
        helper.setText(content, true);
        mailSender.send(message);
        return true;
    }

    public static String generateUrl(HttpServletRequest request) {

        // http://localhost:8080/forgot-password
        String siteUrl = request.getRequestURL().toString();

        return siteUrl.replace(request.getServletPath(), "");
    }

    String msg = null;;

    public Boolean sendMailForProductOrder(ProductOrder order, String status) throws Exception {

        msg = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e6e6e6; border-radius: 5px;'>"
                + "<div style='text-align: center; margin-bottom: 20px;'>"
                + "<img src='https://agristore-profile.s3.eu-north-1.amazonaws.com/AgriStore.png' alt='AgriStore Logo' style='max-width: 150px;'>"
                + "</div>"
                + "<h2 style='color: #2e7d32; text-align: center;'>Order [[orderStatus]]</h2>"
                + "<p style='font-size: 16px;'>Dear [[name]],</p>"
                + "<p style='font-size: 16px;'>Thank you for shopping with AgriStore! Your order has been <strong>[[orderStatus]]</strong>.</p>"
                + "<div style='background-color: #f9f9f9; padding: 15px; border-radius: 5px; margin: 20px 0;'>"
                + "<h3 style='color: #2e7d32; margin-top: 0;'>Order Summary</h3>"
                + "<table style='width: 100%; border-collapse: collapse;'>"
                + "<tr><td style='padding: 8px 0;'><strong>Product:</strong></td><td style='padding: 8px 0;'>[[productName]]</td></tr>"
                + "<tr><td style='padding: 8px 0;'><strong>Category:</strong></td><td style='padding: 8px 0;'>[[category]]</td></tr>"
                + "<tr><td style='padding: 8px 0;'><strong>Quantity:</strong></td><td style='padding: 8px 0;'>[[quantity]]</td></tr>"
                + "<tr><td style='padding: 8px 0;'><strong>Price:</strong></td><td style='padding: 8px 0;'>₹[[price]]</td></tr>"
                + "<tr><td style='padding: 8px 0;'><strong>Payment Method:</strong></td><td style='padding: 8px 0;'>[[paymentType]]</td></tr>"
                + "</table>"
                + "</div>"
                + "<p style='font-size: 16px;'>We'll keep you updated on your order status. If you have any questions, please don't hesitate to contact our customer support team.</p>"
                + "<div style='text-align: center; margin-top: 30px;'>"
                + "<a href='http://agri-estore.eu-north-1.elasticbeanstalk.com' style='background-color: #2e7d32; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; font-weight: bold;'>Track Your Order</a>"
                + "</div>"
                + "<div style='margin-top: 30px; padding-top: 20px; border-top: 1px solid #e6e6e6; text-align: center; color: #666; font-size: 14px;'>"
                + "<p>Thank you for supporting AgriStore!</p>"
                + "<p>© 2025 AgriStore. All rights reserved.</p>"
                + "</div>"
                + "</div>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("yesiamgr8haha@gmail.com", "AgriStore");
        helper.setTo(order.getOrderAddress().getEmail());

        msg = msg.replace("[[name]]", order.getOrderAddress().getFirstName());
        msg = msg.replace("[[orderStatus]]", status);
        msg = msg.replace("[[productName]]", order.getProduct().getTitle());
        msg = msg.replace("[[category]]", order.getProduct().getCategory());
        msg = msg.replace("[[quantity]]", order.getQuantity().toString());
        msg = msg.replace("[[price]]", order.getPrice().toString());
        msg = msg.replace("[[paymentType]]", order.getPaymentType());

        byte[] pdfBytes = generatePdfReceipt(order);
        ByteArrayResource pdf = new ByteArrayResource(pdfBytes);
        helper.addAttachment("Order_Receipt_" + order.getOrderId() + ".pdf", pdf);

        helper.setSubject("Product Order Status");
        helper.setText(msg, true);
        mailSender.send(message);
        return true;
    }

    // pdf generation
    private byte[] generatePdfReceipt(ProductOrder order) throws Exception {
        // Create a new document and output stream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, baos);

        // Open the document
        document.open();

        // Add metadata
        document.addTitle("Order Receipt");
        document.addSubject("Receipt for order " + order.getOrderId());
        document.addKeywords("AgriStore, Order, Receipt");
        document.addCreator("AgriStore");

        // Add logo
        try {
            @SuppressWarnings("deprecation")
            Image logo = Image
                    .getInstance(new URL("https://agristore-profile.s3.eu-north-1.amazonaws.com/AgriStore.png"));
            logo.scaleToFit(150, 150);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);
        } catch (Exception e) {
            // Handle exception if logo can't be loaded
        }

        // Add title
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY);
        Paragraph title = new Paragraph("RECEIPT", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Add order information
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

        document.add(new Paragraph("Order ID: " + order.getOrderId(), headerFont));
        document.add(new Paragraph("Date: " + order.getOrderDate(), normalFont));
        document.add(new Paragraph("Status: " + order.getStatus(), normalFont));
        document.add(new Paragraph("\n"));

        // Add customer information
        document.add(new Paragraph("CUSTOMER DETAILS", headerFont));
        document.add(new Paragraph(
                "Name: " + order.getOrderAddress().getFirstName() + " " + order.getOrderAddress().getLastName(),
                normalFont));
        document.add(new Paragraph("Email: " + order.getOrderAddress().getEmail(), normalFont));
        document.add(new Paragraph("Phone: " + order.getOrderAddress().getMobileNo(), normalFont));
        document.add(new Paragraph("Address: " + order.getOrderAddress().getAddress() + ", " +
                order.getOrderAddress().getCity() + ", " +
                order.getOrderAddress().getState() + " - " +
                order.getOrderAddress().getPincode(), normalFont));
        document.add(new Paragraph("\n"));

        // Create product table
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Set column widths
        float[] columnWidths = { 1.5f, 0.5f, 1f, 1f };
        table.setWidths(columnWidths);

        // Add table headers
        Font tableHeaderFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
        BaseColor headerColor = new BaseColor(46, 125, 50); // Green color (#2e7d32)

        PdfPCell cell1 = new PdfPCell(new Phrase("Product", tableHeaderFont));
        cell1.setBackgroundColor(headerColor);
        cell1.setPadding(8);

        PdfPCell cell2 = new PdfPCell(new Phrase("Qty", tableHeaderFont));
        cell2.setBackgroundColor(headerColor);
        cell2.setPadding(8);

        PdfPCell cell3 = new PdfPCell(new Phrase("Price", tableHeaderFont));
        cell3.setBackgroundColor(headerColor);
        cell3.setPadding(8);

        PdfPCell cell4 = new PdfPCell(new Phrase("Total", tableHeaderFont));
        cell4.setBackgroundColor(headerColor);
        cell4.setPadding(8);

        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);

        // Add product details
        PdfPCell productCell = new PdfPCell(
                new Phrase(order.getProduct().getTitle() + "\n(" + order.getProduct().getCategory() + ")", normalFont));
        productCell.setPadding(8);

        PdfPCell qtyCell = new PdfPCell(new Phrase(order.getQuantity().toString(), normalFont));
        qtyCell.setPadding(8);
        qtyCell.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell priceCell = new PdfPCell(new Phrase("₹" + order.getPrice().toString(), normalFont));
        priceCell.setPadding(8);
        priceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

        double total = order.getPrice() * order.getQuantity();
        PdfPCell totalCell = new PdfPCell(new Phrase("₹" + String.format("%.2f", total), normalFont));
        totalCell.setPadding(8);
        totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

        table.addCell(productCell);
        table.addCell(qtyCell);
        table.addCell(priceCell);
        table.addCell(totalCell);

        document.add(table);

        // Add payment information
        document.add(new Paragraph("PAYMENT INFORMATION", headerFont));
        document.add(new Paragraph("Payment Method: " + order.getPaymentType(), normalFont));
        document.add(new Paragraph("\n"));

        // Add footer
        Font footerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY);
        Paragraph footer = new Paragraph("Thank you for shopping with AgriStore!", footerFont);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);

        // Close the document
        document.close();

        return baos.toByteArray();
    }

    public UserDtls getLoggedInUserDetails(Principal p) {
        String email = p.getName();
        UserDtls userDtls = userService.getUserByEmail(email);
        return userDtls;
    }

    public String getImageUrl(MultipartFile file, Integer bucketType) {

        String bucketName = null;

        if (bucketType == 1) {
            bucketName = categoryBucket;
        } else if (bucketType == 2) {
            bucketName = productBucket;
        } else {
            bucketName = profileBucket;
        }
        // https://shooping-cart-category.s3.amazonaws.com/mobile.jpg

        String imageName = file != null ? file.getOriginalFilename() : "default.jpg";

        String url = "https://" + bucketName + ".s3.amazonaws.com/" + imageName;
        return url;
    }

}