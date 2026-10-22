package br.com.fiap.ClyvoPet.repository;

import br.com.fiap.ClyvoPet.model.Animal;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class AnimalRepository {

    private final JdbcTemplate jdbcTemplate;

    public AnimalRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void salvar(Animal animal) {

        String sql = """
                INSERT INTO ANIMAIS
                (NOME, ESPECIE, RACA, IDADE, PESO, SEXO,
                 TUTOR_NOME, TUTOR_TELEFONE, TUTOR_EMAIL, OBSERVACOES)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.update(
                sql,
                animal.getNome(),
                animal.getEspecie(),
                animal.getRaca(),
                animal.getIdade(),
                animal.getPeso(),
                animal.getSexo(),
                animal.getTutorNome(),
                animal.getTutorTelefone(),
                animal.getTutorEmail(),
                animal.getObservacoes()
        );
    }

    public List<Animal> listarTodos() {

        String sql = """
                SELECT ID,
                       NOME,
                       ESPECIE,
                       RACA,
                       IDADE,
                       PESO,
                       SEXO,
                       TUTOR_NOME,
                       TUTOR_TELEFONE,
                       TUTOR_EMAIL,
                       DATA_CADASTRO,
                       OBSERVACOES,
                       ATIVO
                FROM ANIMAIS
                WHERE ATIVO = 1
                ORDER BY NOME
                """;

        return jdbcTemplate.query(
                sql,
                this::mapear
        );
    }

    public Optional<Animal> buscarPorId(Long id) {

        String sql = """
                SELECT ID,
                       NOME,
                       ESPECIE,
                       RACA,
                       IDADE,
                       PESO,
                       SEXO,
                       TUTOR_NOME,
                       TUTOR_TELEFONE,
                       TUTOR_EMAIL,
                       DATA_CADASTRO,
                       OBSERVACOES,
                       ATIVO
                FROM ANIMAIS
                WHERE ID = ?
                """;

        List<Animal> animais = jdbcTemplate.query(
                sql,
                this::mapear,
                id
        );

        return animais.stream().findFirst();
    }

    public List<Animal> buscarPorEspecie(String especie) {

        String sql = """
                SELECT ID,
                       NOME,
                       ESPECIE,
                       RACA,
                       IDADE,
                       PESO,
                       SEXO,
                       TUTOR_NOME,
                       TUTOR_TELEFONE,
                       TUTOR_EMAIL,
                       DATA_CADASTRO,
                       OBSERVACOES,
                       ATIVO
                FROM ANIMAIS
                WHERE ESPECIE = ?
                  AND ATIVO = 1
                ORDER BY NOME
                """;

        return jdbcTemplate.query(
                sql,
                this::mapear,
                especie
        );
    }

    public void atualizar(Animal animal) {

        String sql = """
                UPDATE ANIMAIS
                SET NOME = ?,
                    ESPECIE = ?,
                    RACA = ?,
                    IDADE = ?,
                    PESO = ?,
                    SEXO = ?,
                    TUTOR_NOME = ?,
                    TUTOR_TELEFONE = ?,
                    TUTOR_EMAIL = ?,
                    OBSERVACOES = ?
                WHERE ID = ?
                """;

        jdbcTemplate.update(
                sql,
                animal.getNome(),
                animal.getEspecie(),
                animal.getRaca(),
                animal.getIdade(),
                animal.getPeso(),
                animal.getSexo(),
                animal.getTutorNome(),
                animal.getTutorTelefone(),
                animal.getTutorEmail(),
                animal.getObservacoes(),
                animal.getId()
        );
    }

    public void deletar(Long id) {

        String sql = """
                UPDATE ANIMAIS
                SET ATIVO = 0
                WHERE ID = ?
                """;

        jdbcTemplate.update(sql, id);
    }

    private Animal mapear(ResultSet rs, int rowNum) throws SQLException {

        Animal animal = new Animal();

        animal.setId(rs.getLong("ID"));
        animal.setNome(rs.getString("NOME"));
        animal.setEspecie(rs.getString("ESPECIE"));
        animal.setRaca(rs.getString("RACA"));

        int idade = rs.getInt("IDADE");
        animal.setIdade(rs.wasNull() ? null : idade);

        double peso = rs.getDouble("PESO");
        animal.setPeso(rs.wasNull() ? null : peso);

        animal.setSexo(rs.getString("SEXO"));
        animal.setTutorNome(rs.getString("TUTOR_NOME"));
        animal.setTutorTelefone(rs.getString("TUTOR_TELEFONE"));
        animal.setTutorEmail(rs.getString("TUTOR_EMAIL"));

        if (rs.getTimestamp("DATA_CADASTRO") != null) {
            animal.setDataCadastro(
                    rs.getTimestamp("DATA_CADASTRO").toLocalDateTime()
            );
        }

        animal.setObservacoes(rs.getString("OBSERVACOES"));
        animal.setAtivo(rs.getInt("ATIVO") == 1);

        return animal;
    }
}