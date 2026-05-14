USE ARTHAFLOW;

ALTER TABLE kyc_details
    ADD COLUMN IF NOT EXISTS rejection_remarks TEXT AFTER terms_accepted;
