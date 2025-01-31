package org.example.demo;

import Sistema.Plano;
import Sistema.PlanoPremium;
import Sistema.PlanoStandard;
import Sistema.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PlanosController {

    @FXML
    private Button standardButton;

    @FXML
    private Button premiumButton;

    private Usuario user;

    @FXML
    void alterarPlanoStandard(ActionEvent event) {
        PlanoStandard planoStandard = new PlanoStandard();
        user.setPlanoAtual(planoStandard);
        System.out.println("Plano Standard selecionado.");
        atualizarPlanoUsuario();
    }

    @FXML
    void alterarPlanoPremium(ActionEvent event) {
        PlanoPremium planoPremium = new PlanoPremium();
        user.setPlanoAtual(planoPremium);
        System.out.println("Plano Premium selecionado.");
        atualizarPlanoUsuario();
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    private void atualizarPlanoUsuario() {
        System.out.println("Plano do usuário atualizado para: " + user.getPlanoAtual().getTipo());
    }
}
