package br.com.fiap.ClyvoPet.repository;

import br.com.fiap.ClyvoPet.model.Lembrete;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class LembreteRepository {

    private final JdbcTemplate jdbcTemplate;

    public LembreteRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // =========================
    // SALVAR
    // =========================
    public void salvar(Lembrete lembrete) {

        String sql = """
                INSERT INTO LEMBRETES (
                    CONSULTA_ID,
                    TUTOR_EMAIL,
                    TUTOR_TELEFONE,
                    MENSAGEM,
                    TIPO
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {

            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    new String[]{"ID"}
            );

            ps.setLong(1, lembrete.getConsultaId());
            ps.setString(2, lembrete.getTutorEmail());
            ps.setString(3, lembrete.getTutorTelefone());
            ps.setString(4, lembrete.getMensagem());
            ps.setString(5, lembrete.getTipo());

            return ps;

        }, keyHolder);

        Number idGerado = keyHolder.getKey();

        if (idGerado == null) {
            throw new IllegalStateException(
                    "Não foi possível recuperar o ID do lembrete criado."
            );
        }

        lembrete.setId(idGerado.longValue());
    }

    // =========================
    // LISTAR TODOS
    // =========================
    public List<Lembrete> listarTodos() {

        String sql = """
                SELECT
                    L.ID,
                    L.CONSULTA_ID,
                    L.TUTOR_EMAIL,
                    L.TUTOR_TELEFONE,
                    L.MENSAGEM,
                    L.DATA_ENVIO,
                    L.ENVIADO,
                    L.TIPO,
                    A.NOME AS ANIMAL_NOME,
                    A.TUTOR_NOME AS TUTOR_NOME
                FROM LEMBRETES L
                INNER JOIN CONSULTAS C
                    ON C.ID = L.CONSULTA_ID
                INNER JOIN ANIMAIS A
                    ON A.ID = C.ANIMAL_ID
                ORDER BY L.DATA_ENVIO DESC
                """;

        return jdbcTemplate.query(
                sql,
                this::mapearLembrete
        );
    }

    // =========================
    // BUSCAR POR ID
    // =========================
    public Optional<Lembrete> buscarPorId(Long id) {

        String sql = """
                SELECT
                    L.ID,
                    L.CONSULTA_ID,
                    L.TUTOR_EMAIL,
                    L.TUTOR_TELEFONE,
                    L.MENSAGEM,
                    L.DATA_ENVIO,
                    L.ENVIADO,
                    L.TIPO,
                    A.NOME AS ANIMAL_NOME,
                    A.TUTOR_NOME AS TUTOR_NOME
                FROM LEMBRETES L
                INNER JOIN CONSULTAS C
                    ON C.ID = L.CONSULTA_ID
                INNER JOIN ANIMAIS A
                    ON A.ID = C.ANIMAL_ID
                WHERE L.ID = ?
                """;

        List<Lembrete> lembretes =
                jdbcTemplate.query(
                        sql,
                        this::mapearLembrete,
                        id
                );

        return lembretes.stream().findFirst();
    }

    // =========================
    // BUSCAR POR CONSULTA
    // =========================
    public List<Lembrete> buscarPorConsulta(Long consultaId) {

        String sql = """
                SELECT
                    L.ID,
                    L.CONSULTA_ID,
                    L.TUTOR_EMAIL,
                    L.TUTOR_TELEFONE,
                    L.MENSAGEM,
                    L.DATA_ENVIO,
                    L.ENVIADO,
                    L.TIPO,
                    A.NOME AS ANIMAL_NOME,
                    A.TUTOR_NOME AS TUTOR_NOME
                FROM LEMBRETES L
                INNER JOIN CONSULTAS C
                    ON C.ID = L.CONSULTA_ID
                INNER JOIN ANIMAIS A
                    ON A.ID = C.ANIMAL_ID
                WHERE L.CONSULTA_ID = ?
                ORDER BY L.DATA_ENVIO DESC
                """;

        return jdbcTemplate.query(
                sql,
                this::mapearLembrete,
                consultaId
        );
    }

    // =========================
    // BUSCAR PENDENTES
    // =========================
    public List<Lembrete> buscarPendentes() {

        String sql = """
                SELECT
                    L.ID,
                    L.CONSULTA_ID,
                    L.TUTOR_EMAIL,
                    L.TUTOR_TELEFONE,
                    L.MENSAGEM,
                    L.DATA_ENVIO,
                    L.ENVIADO,
                    L.TIPO,
                    A.NOME AS ANIMAL_NOME,
                    A.TUTOR_NOME AS TUTOR_NOME
                FROM LEMBRETES L
                INNER JOIN CONSULTAS C
                    ON C.ID = L.CONSULTA_ID
                INNER JOIN ANIMAIS A
                    ON A.ID = C.ANIMAL_ID
                WHERE L.ENVIADO = 0
                ORDER BY L.DATA_ENVIO
                """;

        return jdbcTemplate.query(
                sql,
                this::mapearLembrete
        );
    }

    // =========================
    // MARCAR COMO ENVIADO
    // =========================
    public void marcarComoEnviado(Long id) {

        String sql = """
                UPDATE LEMBRETES
                SET ENVIADO = 1,
                    DATA_ENVIO = SYSDATE
                WHERE ID = ?
                """;

        jdbcTemplate.update(
                sql,
                id
        );
    }

    // =========================
    // MAPPER
    // =========================
    private Lembrete mapearLembrete(
            ResultSet rs,
            int rowNum) throws SQLException {

        Lembrete lembrete = new Lembrete();

        lembrete.setId(
                rs.getLong("ID")
        );

        lembrete.setConsultaId(
                rs.getLong("CONSULTA_ID")
        );

        lembrete.setTutorEmail(
                rs.getString("TUTOR_EMAIL")
        );

        lembrete.setTutorTelefone(
                rs.getString("TUTOR_TELEFONE")
        );

        lembrete.setMensagem(
                rs.getString("MENSAGEM")
        );

        if (rs.getTimestamp("DATA_ENVIO") != null) {
            lembrete.setDataEnvio(
                    rs.getTimestamp("DATA_ENVIO")
                            .toLocalDateTime()
            );
        }

        lembrete.setEnviado(
                rs.getInt("ENVIADO") == 1
        );

        lembrete.setTipo(
                rs.getString("TIPO")
        );

        lembrete.setAnimalNome(
                rs.getString("ANIMAL_NOME")
        );

        lembrete.setTutorNome(
                rs.getString("TUTOR_NOME")
        );

        return lembrete;
    }
}