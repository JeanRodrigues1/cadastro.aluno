/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import factory.ConnectionFactory;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import modelo.Aluno;

/**
 *
 * @author usuario
 */
public class AlunoDAO {
    private Connection connection;
    
    public AlunoDAO(){
        this.connection = new ConnectionFactory().getConexaoMySQL();
    }
      
    public void adicionaAluno(Aluno aluno){
        String sql = "INSERT INTO aluno(cpf, nome, dataN, peso, altura) VALUES(?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, aluno.getCpf());
            stmt.setString(2, aluno.getNome());
            stmt.setString(3, aluno.getDataNascimento());
            stmt.setInt(4, aluno.getPeso());
            stmt.setInt(5, aluno.getAltura());
            stmt.execute();
            stmt.close();
        }
        catch (SQLException u){
            throw new RuntimeException(u);
        }
    }
    
     public void deletaAluno(String cpf) {
        String sql = "DELETE FROM aluno WHERE cpf = (?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cpf);
            int rowsAffected = statement.executeUpdate();
            System.out.println(rowsAffected + " colunas afetadas.");
            System.out.println("Aluno de cpf " + cpf + " excluído com sucesso.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }   
    
    public void atualizarAluno(Aluno aluno) {
        String sql = "UPDATE aluno SET nome = ?,peso = ?,altura = ? WHERE cpf = ?;";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, aluno.getNome());
            stmt.setInt(2, aluno.getPeso());
            stmt.setInt(3, aluno.getAltura());
            stmt.setString(4, aluno.getCpf());

            stmt.execute();
            stmt.close();
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }
    public double calculaIMC(String cpf) {
        String sql = "SELECT peso, altura FROM aluno WHERE cpf = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cpf);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    double peso = resultSet.getDouble("peso");
                    double altura = resultSet.getInt("altura");
                    double imc = peso / ((altura / 100) * (altura / 100));
                    return Math.round(imc * 100.0) / 100.0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    } 
 
    public List<Aluno> buscarAlunos(String nome) {
        List<Aluno> alunos = new ArrayList<>();
        String sql = "SELECT * from escola.aluno";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String cpf = rs.getString("cpf");
      //              String nome = rs.getString("nome");
                    String DataNascimento = rs.getString("dataN");
                    int peso = rs.getInt("peso");
                    int altura = rs.getInt("altura");
                    
                    Aluno novoAluno = new Aluno(cpf, nome, DataNascimento, peso, altura);
                    alunos.add(novoAluno);

                }
            } catch (SQLException e) {
                throw new RuntimeException(e);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
        return alunos;

    }
    
}
