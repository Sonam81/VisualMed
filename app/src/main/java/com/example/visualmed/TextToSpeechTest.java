package com.example.visualmed;

import android.speech.tts.SynthesisCallback;
import android.speech.tts.SynthesisRequest;
import android.speech.tts.TextToSpeechService;

public class TextToSpeechTest extends TextToSpeechService {
    @Override
    protected int onIsLanguageAvailable(String lang, String country, String variant) {
        return 0;
    }

    @Override
    protected String[] onGetLanguage() {
        return new String[0];
    }

    @Override
    protected int onLoadLanguage(String lang, String country, String variant) {
        return 0;
    }

    @Override
    protected void onStop() {

    }

    @Override
    protected void onSynthesizeText(SynthesisRequest request, SynthesisCallback callback) {

    }
}
