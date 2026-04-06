-- Script para limpar o banco de dados entre testes (PostgreSQL)
-- Executa antes de cada método de teste

-- Trunca todas as tabelas com CASCADE (respeita FK automaticamente)
TRUNCATE TABLE payments, order_items, orders, products, users CASCADE;
