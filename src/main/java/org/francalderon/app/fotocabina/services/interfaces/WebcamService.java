package org.francalderon.app.fotocabina.services.interfaces;

import javafx.scene.image.ImageView;
import org.francalderon.app.fotocabina.models.Temporizador;
import org.francalderon.app.fotocabina.models.enums.AspectRatio;

public interface WebcamService {
    void start();

    void stop();

    AspectRatio getAspectRatio();

    void setAspectRatio(AspectRatio aspectRatio);

    Temporizador getTemporizador();

    void setImageView(ImageView imageLive);

    ImageView getMiniPreview();

    void tomarFotosConTemporizador();

    void setModoEspejo(boolean selected);
}
