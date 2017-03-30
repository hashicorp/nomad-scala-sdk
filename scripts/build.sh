#!/usr/bin/env bash
set -euo pipefail

exec sbt +compile +test:compile +test +scalastyle
