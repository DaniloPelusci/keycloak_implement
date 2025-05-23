-- Tabela de usuários (usuários do sistema, vinculados ao Keycloak)
CREATE TABLE usuario (
    id UUID PRIMARY KEY, -- ID do Keycloak
    nome VARCHAR(150) NOT NULL,
    telefone VARCHAR(20),
    email VARCHAR(100)
);

-- Tabela de ocupações (funções no sistema: corretor, gerente, etc.)
CREATE TABLE ocupacao (
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL
);

-- Associação muitos-para-muitos entre usuário e ocupação
CREATE TABLE usuario_ocupacao (
    usuario_id UUID NOT NULL,
    ocupacao_id INT NOT NULL,
    PRIMARY KEY (usuario_id, ocupacao_id),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (ocupacao_id) REFERENCES ocupacao(id) ON DELETE CASCADE
);

-- Tabela de leads
CREATE TABLE leads (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    cpf_cnpj VARCHAR(20),
    telefone VARCHAR(20),
    origem VARCHAR(100),
    status_leads VARCHAR(50),
    observacao TEXT,
    corretor_id UUID,
    FOREIGN KEY (corretor_id) REFERENCES usuario(id) ON DELETE SET NULL
);

-- Tabela de documentos associados a leads
CREATE TABLE documentos_lead (
    id SERIAL PRIMARY KEY,
    nome_arquivo VARCHAR(200) NOT NULL,
    tipo_arquivo VARCHAR(20),
    data_upload TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    conteudo BYTEA NOT NULL, -- Para PostgreSQL; use LONGBLOB se for MySQL
    lead_id INT NOT NULL,
    FOREIGN KEY (lead_id) REFERENCES leads(id) ON DELETE CASCADE
);

-- Histórico de alterações nos leads
CREATE TABLE lead_historico (
    id SERIAL PRIMARY KEY,
    lead_id INT NOT NULL,
    data_modificacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    acao TEXT NOT NULL,
    usuario_id UUID,
    FOREIGN KEY (lead_id) REFERENCES leads(id) ON DELETE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE SET NULL
);
