package com.example.androidexample;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.androidexample.features.items.BarcodeScanner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

@RunWith(AndroidJUnit4.class)
public class ScannerTest {

    // ─── Test 1: Parse a valid ZXing HTML response ────────────────────────────
    @Test
    public void testParseHtml_validResponse_returnsBarcode() {
        try (ActivityScenario<BarcodeScanner> scenario = ActivityScenario.launch(BarcodeScanner.class)) {
            scenario.onActivity(scanner -> {
                String fakeHtml = "<html><head><title>Decode Succeeded</title></head>"
                        + "<body><table><tr><td>Raw text</td>"
                        + "<td><pre>051111407592</pre></td></tr></table></body></html>";

                scanner.parseHtmlForValueAndShowResult(fakeHtml);

                assertEquals("051111407592", scanner.hintView.getText().toString());
            });
        }
    }

    // ─── Test 2: Parse a failed response ─────────────────────────────────────
    @Test
    public void testParseHtml_failedResponse_showsError() {
        try (ActivityScenario<BarcodeScanner> scenario = ActivityScenario.launch(BarcodeScanner.class)) {
            scenario.onActivity(scanner -> {
                String fakeHtml = "<html><head><title>Decode Failed</title></head><body></body></html>";

                scanner.parseHtmlForValueAndShowResult(fakeHtml);

                assertEquals("No Barcode Found Try again", scanner.hintView.getText().toString());
            });
        }
    }
}