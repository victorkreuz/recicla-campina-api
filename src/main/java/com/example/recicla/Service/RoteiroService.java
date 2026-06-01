package com.example.recicla.Service;

import com.example.recicla.Entity.Roteiro;
import com.example.recicla.Entity.Usuario;
import com.example.recicla.Repository.UsuarioRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class RoteiroService {

    private static final Logger logger = LoggerFactory.getLogger(RoteiroService.class);

    private final UsuarioRepository usuarioRepository;
    private final JavaMailSender mailSender;

    @Value("${recicla.alerta.origem}")
    private String origemEmail;

    public RoteiroService(UsuarioRepository usuarioRepository, JavaMailSender mailSender) {
        this.usuarioRepository = usuarioRepository;
        this.mailSender = mailSender;
    }

    public int enviarAlertas(Roteiro roteiro) {
        List<Usuario> usuarios = usuarioRepository.findByEnderecoContainingIgnoreCase(roteiro.getArea());
        logger.info("Enviando alerta de coleta para {} usuário(s) da área {}", usuarios.size(), roteiro.getArea());
        for (Usuario usuario : usuarios) {
            enviarEmailComHtml(usuario, roteiro);
        }
        return usuarios.size();
    }

    private void enviarEmailComHtml(Usuario usuario, Roteiro roteiro) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(origemEmail);
            helper.setTo(usuario.getEmail());
            helper.setSubject("🚨 Aviso de coleta de resíduos!");

            String htmlMsg = formatarMensagem(roteiro, usuario.getNome());
            helper.setText(htmlMsg, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException | MailException e) {
            throw new IllegalStateException("Não foi possível enviar o alerta para " + usuario.getEmail(), e);
        }
    }


    private String formatarMensagem(Roteiro roteiro, String nomeUsuario) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

        String conteudoHtml;
        String tipoLixo = roteiro.getTipoLixo().toLowerCase(Locale.ROOT);

        switch (tipoLixo) {
            case "seco":
                conteudoHtml = "<p>Olá <strong>" + nomeUsuario + "</strong>! Tudo certo?</p>" +
                        "<p>📅  O recolhimento do <strong>lixo seco/reciclável</strong> será em:<br>" +
                        "🗓  " + roteiro.getDataEHora().format(formatter) + "</p>" +
                        "<p>🚮 Separe corretamente os materiais recicláveis e coloque-os para fora com antecedência.<br>" +
                        "✅  Inclui: papel, plástico, vidro limpo e metais.</p>";
                break;

            case "organico":
                conteudoHtml = "<p>Olá <strong>" + nomeUsuario + "</strong>! Tudo bem?</p>" +
                        "<p>📅  O recolhimento do <strong>lixo orgânico</strong> está agendado para:<br>" +
                        "🗓  " + roteiro.getDataEHora().format(formatter) + "</p>" +
                        "<p>🚮  Por favor, coloque o lixo para fora até 30 minutos antes do horário.<br>" +
                        "✅  Lembre-se: lixo orgânico inclui restos de comida, cascas de frutas, borra de café, etc.</p>";
                break;

            case "outro":
                conteudoHtml = "<p>Olá <strong>" + nomeUsuario + "</strong>, " + roteiro.getMensagem() + "</p>";
                break;

            default:
                conteudoHtml = "<p><strong>Tipo de lixo não reconhecido.</strong></p>";
        }

        // Envolvendo o conteúdo em tags HTML e BODY
        return "<html><body style=\"font-family: Arial, sans-serif; font-size: 14px; color: #333;\">" +
                conteudoHtml +
                "</body></html>";
    }
}

