# Configuração de CI/CD com GitHub Actions

## 📋 Visão Geral

Esta pipeline implementa um fluxo completo de CI/CD com as seguintes etapas:

1. **Build**: Compilação e testes da aplicação
2. **Code Quality**: Análise de código com SonarQube
3. **Docker Build**: Build da imagem Docker
4. **Security Scan**: Análise de vulnerabilidades

## 🔐 Configuração de Secrets

Você precisa configurar os seguintes secrets no GitHub (Settings → Secrets and variables → Actions):

### Secrets Necessários (Opcionais)

- **SONAR_HOST_URL**: URL do SonarQube (ex: `https://sonarqube.example.com`)
- **SONAR_TOKEN**: Token de autenticação do SonarQube
- **SNYK_TOKEN**: Token do Snyk para análise de segurança
- **DOCKER_REGISTRY_URL**: URL do Docker Registry (ex: `ghcr.io`)
- **DOCKER_REGISTRY_USERNAME**: Usuário do Docker Registry
- **DOCKER_REGISTRY_TOKEN**: Token/senha do Docker Registry

### Como Adicionar Secrets

1. Vá para: `Settings` → `Secrets and variables` → `Actions`
2. Clique em "New repository secret"
3. Adicione o nome e valor do secret

## 🚀 Fluxos de Execução

### Quando a Pipeline Executa

- **Push em main ou develop**: Executa todas as jobs
- **Pull Request**: Executa build, testes e análise de código
- **Manual**: Pode ser disparada manualmente via GitHub Actions

## 📊 Relatórios Gerados

### Cobertura de Testes
- Artefato: `jacoco.xml`
- Enviado automaticamente para CodeCov
- Visualizar em: https://codecov.io

### Análise de Código
- Via SonarQube (se configurado)
- Relatório disponível no dashboard do SonarQube

### Vulnerabilidades
- OWASP Dependency Check
- Snyk Security Scanner
- Relatórios salvos como artefatos

## 🐳 Build Docker

A imagem Docker é construída automaticamente em pushes com tags:
- `ecommerce:latest` (tag mais recente)
- `ecommerce:<commit-sha>` (versão específica)

## 📝 Personalizações

### Habilitar SonarQube
Descomente a job `code-quality` se tiver SonarQube configurado.

### Publicar no Docker Registry
Adicione ao workflow:

```yaml
- name: Login to Docker Hub
  uses: docker/login-action@v2
  with:
    username: ${{ secrets.DOCKER_USERNAME }}
    password: ${{ secrets.DOCKER_PASSWORD }}

- name: Push Docker image
  run: |
    docker push ecommerce:latest
    docker push ecommerce:${{ github.sha }}
```

### Adicionar Deploy
Crie uma nova job para deploy em homologação/produção:

```yaml
deploy:
  runs-on: ubuntu-latest
  needs: docker-build
  if: github.ref == 'refs/heads/main'
  steps:
    # Deploy steps aqui
```

## ✅ Checklist de Configuração

- [ ] Arquivo `.github/workflows/ci-cd.yml` criado
- [ ] `Dockerfile` configurado
- [ ] `.dockerignore` criado
- [ ] (Opcional) Secrets do SonarQube adicionados
- [ ] (Opcional) Secrets do Docker Registry adicionados
- [ ] (Opcional) Secrets do Snyk adicionados
- [ ] Fazer um push para testar a pipeline

## 🔍 Monitoramento

- Acessar "Actions" no repositório GitHub para ver execuções
- Visualizar logs detalhados de cada job
- Verificar badges de status nas releases

## 📚 Referências

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [SonarQube Docker](https://docs.sonarqube.org/)
- [Snyk CLI](https://docs.snyk.io/snyk-cli)
- [OWASP Dependency Check](https://owasp.org/www-project-dependency-check/)
