package br.com.fiap.ClyvoPet.repository;

import br.com.fiap.ClyvoPet.model.Consulta;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ConsultaRepository {

    private final JdbcTemplate jdbcTemplate;

    public ConsultaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void salvar(Consulta consulta) {

        String sql = """
                INSERT INTO CONSULTAS
                (
                    ANIMAL_ID,
                    VETERINARIO_ID,
                    DATA_HORA,
                    MOTIVO,
                    DIAGNOSTICO,
                    PRESCRICAO,
                    STATUS
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.update(
                sql,
                consulta.getAnimalId(),
                consulta.getVeterinarioId(),
                Timestamp.valueOf(consulta.getDataHora()),
                consulta.getMotivo(),
                consulta.getDiagnostico(),
                consulta.getPrescricao(),
                consulta.getStatus()
        );
    }

    public List<Consulta> listarTodos() {

        String sql = """
                SELECT
                    C.ID,
                    C.ANIMAL_ID,
                    C.VETERINARIO_ID,
                    C.DATA_HORA,
                    C.MOTIVO,
                    C.DIAGNOSTICO,
                    C.PRESCRICAO,
                    C.STATUS,
                    C.LEMBRETE_ENVIADO,

                    A.NOME AS ANIMAL_NOME,
                    NVL(V.NOME, 'Veterinário excluído') AS VETERINARIO_NOME

                FROM CONSULTAS C

                INNER JOIN ANIMAIS A
                    ON A.ID = C.ANIMAL_ID

                LEFT JOIN VETERINARIOS V
                    ON V.ID = C.VETERINARIO_ID

                ORDER BY C.DATA_HORA DESC
                """;

        return jdbcTemplate.query(
                sql,
                this::mapear
        );
    }

    public Optional<Consulta> buscarPorId(Long id) {

        String sql = """
                SELECT
                    C.ID,
                    C.ANIMAL_ID,
                    C.VETERINARIO_ID,
                    C.DATA_HORA,
                    C.MOTIVO,
                    C.DIAGNOSTICO,
                    C.PRESCRICAO,
                    C.STATUS,
                    C.LEMBRETE_ENVIADO,

                    A.NOME AS ANIMAL_NOME,
                    NVL(V.NOME, 'Veterinário excluído') AS VETERINARIO_NOME

                FROM CONSULTAS C

                INNER JOIN ANIMAIS A
                    ON A.ID = C.ANIMAL_ID

                LEFT JOIN VETERINARIOS V
                    ON V.ID = C.VETERINARIO_ID

                WHERE C.ID = ?
                """;

        List<Consulta> consultas = jdbcTemplate.query(
                sql,
                this::mapear,
                id
        );

        return consultas.stream().findFirst();
    }

    public List<Consulta> buscarPorAnimal(Long animalId) {

        String sql = """
                SELECT
                    C.ID,
                    C.ANIMAL_ID,
                    C.VETERINARIO_ID,
                    C.DATA_HORA,
                    C.MOTIVO,
                    C.DIAGNOSTICO,
                    C.PRESCRICAO,
                    C.STATUS,
                    C.LEMBRETE_ENVIADO,

                    A.NOME AS ANIMAL_NOME,
                    NVL(V.NOME, 'Veterinário excluído') AS VETERINARIO_NOME

                FROM CONSULTAS C

                INNER JOIN ANIMAIS A
                    ON A.ID = C.ANIMAL_ID

                LEFT JOIN VETERINARIOS V
                    ON V.ID = C.VETERINARIO_ID

                WHERE C.ANIMAL_ID = ?

                ORDER BY C.DATA_HORA DESC
                """;

        return jdbcTemplate.query(
                sql,
                this::mapear,
                animalId
        );
    }

    public List<Consulta> buscarConsultasPendentesLembrete(
            LocalDateTime inicio,
            LocalDateTime fim) {

        String sql = """
                SELECT
                    C.ID,
                    C.ANIMAL_ID,
                    C.VETERINARIO_ID,
                    C.DATA_HORA,
                    C.MOTIVO,
                    C.DIAGNOSTICO,
                    C.PRESCRICAO,
                    C.STATUS,
                    C.LEMBRETE_ENVIADO,

                    A.NOME AS ANIMAL_NOME,
                    NVL(V.NOME, 'Veterinário excluído') AS VETERINARIO_NOME

                FROM CONSULTAS C

                INNER JOIN ANIMAIS A
                    ON A.ID = C.ANIMAL_ID

                LEFT JOIN VETERINARIOS V
                    ON V.ID = C.VETERINARIO_ID

                WHERE C.LEMBRETE_ENVIADO = 0
                  AND C.STATUS = 'AGENDADA'
                  AND C.DATA_HORA BETWEEN ? AND ?

                ORDER BY C.DATA_HORA
                """;

        return jdbcTemplate.query(
                sql,
                this::mapear,
                Timestamp.valueOf(inicio),
                Timestamp.valueOf(fim)
        );
    }

    public void atualizar(Consulta consulta) {

        String sql = """
                UPDATE CONSULTAS
                SET ANIMAL_ID = ?,
                    VETERINARIO_ID = ?,
                    DATA_HORA = ?,
                    MOTIVO = ?,
                    DIAGNOSTICO = ?,
                    PRESCRICAO = ?,
                    STATUS = ?,
                    LEMBRETE_ENVIADO = ?
                WHERE ID = ?
                """;

        jdbcTemplate.update(
                sql,
                consulta.getAnimalId(),
                consulta.getVeterinarioId(),
                Timestamp.valueOf(consulta.getDataHora()),
                consulta.getMotivo(),
                consulta.getDiagnostico(),
                consulta.getPrescricao(),
                consulta.getStatus(),
                Boolean.TRUE.equals(consulta.getLembreteEnviado()) ? 1 : 0,
                consulta.getId()
        );
    }

    public int cancelarConsultasPorVeterinario(Long veterinarioId) {

        String sql = """
                UPDATE CONSULTAS
                SET STATUS = 'CANCELADA',
                    VETERINARIO_ID = NULL
                WHERE VETERINARIO_ID = ?
                """;

        return jdbcTemplate.update(
                sql,
                veterinarioId
        );
    }

    public void deletar(Long id) {

        String sql = """
                DELETE FROM CONSULTAS
                WHERE ID = ?
                """;

        jdbcTemplate.update(
                sql,
                id
        );
    }

    private Consulta mapear(ResultSet rs, int rowNum) throws SQLException {

        Consulta consulta = new Consulta();

        consulta.setId(
                rs.getLong("ID")
        );

        consulta.setAnimalId(
                rs.getLong("ANIMAL_ID")
        );

        Long veterinarioId = rs.getObject(
                "VETERINARIO_ID",
                Long.class
        );

        consulta.setVeterinarioId(
                veterinarioId
        );

        consulta.setAnimalNome(
                rs.getString("ANIMAL_NOME")
        );

        consulta.setVeterinarioNome(
                rs.getString("VETERINARIO_NOME")
        );

        Timestamp timestamp = rs.getTimestamp("DATA_HORA");

        if (timestamp != null) {
            consulta.setDataHora(
                    timestamp.toLocalDateTime()
            );
        }

        consulta.setMotivo(
                rs.getString("MOTIVO")
        );

        consulta.setDiagnostico(
                rs.getString("DIAGNOSTICO")
        );

        consulta.setPrescricao(
                rs.getString("PRESCRICAO")
        );

        consulta.setStatus(
                rs.getString("STATUS")
        );

        consulta.setLembreteEnviado(
                rs.getInt("LEMBRETE_ENVIADO") == 1
        );

        return consulta;
    }
}