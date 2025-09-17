package com.homedepot.rma.service;

import com.homedepot.rma.model.ReturnRequest;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class QrCodeService {
    
    private static final int QR_CODE_WIDTH = 300;
    private static final int QR_CODE_HEIGHT = 300;
    
    public String generateQrCode(ReturnRequest returnRequest) {
        try {
            String qrData = String.format(
                "RMA:%s|Order:%s|Customer:%s|Method:STORE|Date:%s",
                returnRequest.getRmaNumber(),
                returnRequest.getOrder().getOrderNumber(),
                returnRequest.getCustomer().getCustomerId(),
                returnRequest.getRequestedDate()
            );
            
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrData, BarcodeFormat.QR_CODE, QR_CODE_WIDTH, QR_CODE_HEIGHT);
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            
            byte[] qrCodeBytes = outputStream.toByteArray();
            String base64QrCode = Base64.getEncoder().encodeToString(qrCodeBytes);
            
            return "data:image/png;base64," + base64QrCode;
            
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }
    
    public String generateQrCodeData(String rmaNumber, String orderNumber, String customerId, String method) {
        return String.format(
            "RMA:%s|Order:%s|Customer:%s|Method:%s|Date:%s",
            rmaNumber,
            orderNumber,
            customerId,
            method,
            System.currentTimeMillis()
        );
    }
}
