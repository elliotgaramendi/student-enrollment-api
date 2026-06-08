#!/usr/bin/env bash

set -euo pipefail

BASE_URL="${BASE_URL:-http://localhost:8080}"
API_URL="${BASE_URL}/api/v1"

json_ids() {
  python3 -c 'import json, sys; print("\n".join(str(item["id"]) for item in json.load(sys.stdin)))'
}

delete_resource() {
  local path="$1"
  local id="$2"

  curl -fsS -X DELETE "${API_URL}/${path}/${id}" >/dev/null
  echo "Deleted ${path}/${id}"
}

echo "Resetting demo data through ${API_URL}"

enrollment_ids="$(curl -fsS "${API_URL}/student-enrollments" | json_ids)"
if [[ -n "${enrollment_ids}" ]]; then
  while IFS= read -r enrollment_id; do
    [[ -n "${enrollment_id}" ]] && delete_resource "student-enrollments" "${enrollment_id}"
  done <<< "${enrollment_ids}"
else
  echo "No student enrollments to delete"
fi

student_ids="$(curl -fsS "${API_URL}/students" | json_ids)"
if [[ -n "${student_ids}" ]]; then
  while IFS= read -r student_id; do
    [[ -n "${student_id}" ]] && delete_resource "students" "${student_id}"
  done <<< "${student_ids}"
else
  echo "No students to delete"
fi

echo "Demo data reset complete"
