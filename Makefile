export ENV_VAR

# .PHONY: init
# init:
# 	@command git config core.hooksPath hooks
# 	@command find .git/hooks -type l -exec rm {} \;
# 	@command find hooks -type f -exec ln -sf ../../{} .git/hooks/ \;

.PHONY: start
start:
	@command docker-compose -f docker-compose.yml up -d

.PHONY: stop
stop:
	@command docker-compose -f docker-compose.yml stop