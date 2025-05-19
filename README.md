-- Tabela de usuários (vinculados ao Keycloak)
CREATE TABLE usuario (
    id UUID PRIMARY KEY, -- ID do Keycloak
    nome VARCHAR(150) NOT NULL,
    telefone VARCHAR(20),
    email VARCHAR(100)
);

-- Tabela de ocupações (papéis do sistema: corretor, gerente, etc.)
CREATE TABLE ocupacao (
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL
);

-- Tabela de associação entre usuários e ocupações
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
    telefone VARCHAR(20),
    origem VARCHAR(100),
    status_leads VARCHAR(50),
    corretor_id UUID,
    FOREIGN KEY (corretor_id) REFERENCES usuario(id) ON DELETE SET NULL
);

-- Tabela de documentos dos leads
CREATE TABLE documentos_lead (
    id SERIAL PRIMARY KEY,
    nome_arquivo VARCHAR(200) NOT NULL,
    tipo_arquivo VARCHAR(20),
    data_upload TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    conteudo BYTEA NOT NULL, -- Use LONGBLOB em MySQL
    lead_id INT NOT NULL,
    FOREIGN KEY (lead_id) REFERENCES leads(id) ON DELETE CASCADE
);

-- Tabela de histórico de modificações dos leads
CREATE TABLE lead_historico (
    id SERIAL PRIMARY KEY,
    lead_id INT NOT NULL,
    data_modificacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    acao TEXT NOT NULL,
    usuario_id UUID NOT NULL,
    FOREIGN KEY (lead_id) REFERENCES leads(id) ON DELETE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE SET NULL
);
