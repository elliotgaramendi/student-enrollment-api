#!/usr/bin/env bash

set -euo pipefail

BASE_URL="${BASE_URL:-http://localhost:8080}"
API_URL="${BASE_URL}/api/v1"

create_student() {
  local first_name="$1"
  local last_name="$2"
  local email="$3"

  curl -fsS -X POST "${API_URL}/students" \
    -H "Content-Type: application/json" \
    -d "{
      \"firstName\": \"${first_name}\",
      \"lastName\": \"${last_name}\",
      \"email\": \"${email}\"
    }" | python3 -c 'import json, sys; print(json.load(sys.stdin)["id"])'
}

create_enrollment() {
  local student_id="$1"
  local course_code="$2"
  local enrollment_date="$3"

  curl -fsS -X POST "${API_URL}/student-enrollments" \
    -H "Content-Type: application/json" \
    -d "{
      \"studentId\": ${student_id},
      \"courseCode\": \"${course_code}\",
      \"enrollmentDate\": \"${enrollment_date}\"
    }" >/dev/null
}

seed_student_with_enrollment() {
  local first_name="$1"
  local last_name="$2"
  local email="$3"
  local course_code="$4"
  local enrollment_date="$5"

  local student_id
  student_id="$(create_student "${first_name}" "${last_name}" "${email}")"
  create_enrollment "${student_id}" "${course_code}" "${enrollment_date}"
  echo "Created ${first_name} ${last_name} (${email}) -> ${course_code} on ${enrollment_date}"
}

echo "Seeding demo data through ${API_URL}"

seed_student_with_enrollment "Elliot" "Garamendi" "elliotgaramendi@gmail.com" "AI-ENGINEER" "2026-01-31"
seed_student_with_enrollment "Mijael" "Garamendi" "mijaelgaramendi@gmail.com" "DANCE-101" "2026-05-08"
seed_student_with_enrollment "Fe" "Esperanza" "feesperanza@gmail.com" "PSY-101" "2026-01-01"
seed_student_with_enrollment "Fer" "Oz" "feroz@gmail.com" "PSY-101" "2026-01-29"
seed_student_with_enrollment "Choco" "Late" "chocolate@gmail.com" "GASTRO-101" "2026-06-01"
seed_student_with_enrollment "Doky" "Patita" "dokypatita@gmail.com" "GASTRO-101" "2026-01-08"
seed_student_with_enrollment "NN" "Sarmiento" "nnsarmiento@gmail.com" "GAMER-101" "2026-03-11"
seed_student_with_enrollment "Isa" "Aqui" "isaaqui@gmail.com" "BUS-INTL" "2026-01-29"

echo "Demo data seed complete"
