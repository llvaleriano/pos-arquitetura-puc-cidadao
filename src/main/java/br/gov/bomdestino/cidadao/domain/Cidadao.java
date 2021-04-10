package br.gov.bomdestino.cidadao.domain;

import br.gov.bomdestino.cidadao.domain.enumeration.Sexo;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cidadao")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "cidadao")
public class Cidadao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo")
    private Sexo sexo;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "nascimento")
    private LocalDate nascimento;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private Endereco endereco;

    @OneToMany(mappedBy = "cidadao")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Telefone> telefones = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public Cidadao nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public Cidadao sexo(Sexo sexo) {
        this.sexo = sexo;
        return this;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public String getEmail() {
        return email;
    }

    public Cidadao email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getNascimento() {
        return nascimento;
    }

    public Cidadao nascimento(LocalDate nascimento) {
        this.nascimento = nascimento;
        return this;
    }

    public void setNascimento(LocalDate nascimento) {
        this.nascimento = nascimento;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public Cidadao endereco(Endereco endereco) {
        this.endereco = endereco;
        return this;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Set<Telefone> getTelefones() {
        return telefones;
    }

    public Cidadao telefones(Set<Telefone> telefones) {
        this.telefones = telefones;
        return this;
    }

    public Cidadao addTelefones(Telefone telefone) {
        this.telefones.add(telefone);
        telefone.setCidadao(this);
        return this;
    }

    public Cidadao removeTelefones(Telefone telefone) {
        this.telefones.remove(telefone);
        telefone.setCidadao(null);
        return this;
    }

    public void setTelefones(Set<Telefone> telefones) {
        this.telefones = telefones;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cidadao)) {
            return false;
        }
        return id != null && id.equals(((Cidadao) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cidadao{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", sexo='" + getSexo() + "'" +
            ", email='" + getEmail() + "'" +
            ", nascimento='" + getNascimento() + "'" +
            "}";
    }
}
