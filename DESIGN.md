---
name: Clinical Admin Strategy
colors:
  surface: '#f7f9fb'
  surface-dim: '#d8dadc'
  surface-bright: '#f7f9fb'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f2f4f6'
  surface-container: '#eceef0'
  surface-container-high: '#e6e8ea'
  surface-container-highest: '#e0e3e5'
  on-surface: '#191c1e'
  on-surface-variant: '#434653'
  inverse-surface: '#2d3133'
  inverse-on-surface: '#eff1f3'
  outline: '#737784'
  outline-variant: '#c3c6d5'
  surface-tint: '#1d59c1'
  primary: '#003c90'
  on-primary: '#ffffff'
  primary-container: '#0f52ba'
  on-primary-container: '#bcceff'
  inverse-primary: '#b0c6ff'
  secondary: '#505f76'
  on-secondary: '#ffffff'
  secondary-container: '#d0e1fb'
  on-secondary-container: '#54647a'
  tertiary: '#004943'
  on-tertiary: '#ffffff'
  tertiary-container: '#00635b'
  on-tertiary-container: '#73e0d2'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#d9e2ff'
  primary-fixed-dim: '#b0c6ff'
  on-primary-fixed: '#001945'
  on-primary-fixed-variant: '#00419c'
  secondary-fixed: '#d3e4fe'
  secondary-fixed-dim: '#b7c8e1'
  on-secondary-fixed: '#0b1c30'
  on-secondary-fixed-variant: '#38485d'
  tertiary-fixed: '#89f5e7'
  tertiary-fixed-dim: '#6bd8cb'
  on-tertiary-fixed: '#00201d'
  on-tertiary-fixed-variant: '#005049'
  background: '#f7f9fb'
  on-background: '#191c1e'
  surface-variant: '#e0e3e5'
typography:
  display-lg:
    fontFamily: Public Sans
    fontSize: 36px
    fontWeight: '700'
    lineHeight: 44px
    letterSpacing: -0.02em
  headline-md:
    fontFamily: Public Sans
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
    letterSpacing: -0.01em
  title-sm:
    fontFamily: Public Sans
    fontSize: 18px
    fontWeight: '600'
    lineHeight: 24px
  body-md:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-sm:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  label-bold:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '700'
    lineHeight: 16px
    letterSpacing: 0.05em
  caption:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  base: 4px
  xs: 0.25rem
  sm: 0.5rem
  md: 1rem
  lg: 1.5rem
  xl: 2rem
  gutter: 24px
  margin: 32px
  max-width: 1440px
---

## Brand & Style

The brand personality for this design system is **authoritative, sterile, and hyper-efficient**. It is designed for hospital administrators and medical staff who require a high-density, low-friction interface to manage patient flow. The target audience values precision and reliability over decorative flair.

The visual style follows a **Corporate / Modern** approach with a heavy emphasis on **Minimalism**. By utilizing generous white space and a clinical color palette, the system reduces cognitive load during high-stress registration tasks. The interface should feel like a high-end medical instrument: precise, clean, and uncompromisingly functional.

## Colors

This design system utilizes a "Medical Blue" primary hue, specifically chosen for its associations with trust and stability in healthcare. The palette is dominated by high-luminance whites and soft greys to maintain a "clinical" feel.

- **Primary (#0F52BA):** Used for primary actions, active navigation states, and critical information markers.
- **Secondary (#64748B):** A slate blue-grey for sub-text, icons, and non-critical UI elements.
- **Tertiary (#0D9488):** A teal-green reserved for "Success" states, confirmed appointments, and positive health indicators.
- **Neutral (#F8FAFC):** The foundational background color to differentiate surfaces from the pure white (`#FFFFFF`) of content cards.
- **Status Colors:** Use a high-contrast Red (`#BE123C`) for urgent alerts and Amber (`#B45309`) for pending registrations.

## Typography

This design system prioritizes **Public Sans** for headings due to its institutional and highly legible character, originally designed for government and public-facing interfaces. It conveys a sense of officialdom and security.

For functional text and data entry, **Inter** is used to ensure maximum readability at small sizes, specifically in complex data tables and patient records. Typography is strictly high-contrast, ensuring all body text maintains a minimum 4.5:1 contrast ratio against backgrounds to meet accessibility standards.

## Layout & Spacing

This design system employs a **Fixed Grid** model for the central administrative dashboard to ensure data density remains predictable across professional-grade monitors.

- **Grid:** A 12-column system with 24px gutters.
- **Sidebar:** A fixed 280px navigation rail on the left.
- **Rhythm:** An 8px linear scale (4px, 8px, 16px, 24px, 32px, 48px, 64px) governs all padding and margins to create a logical, mathematical flow.
- **Density:** High density is preferred for "Patient Lists," while "Registration Forms" utilize a more relaxed "xl" spacing to prevent user error during data entry.

## Elevation & Depth

To maintain a secure and professional atmosphere, this design system avoids aggressive shadows. Instead, it uses **Tonal Layers** supplemented by **Ambient Shadows**.

- **Level 0 (Surface):** The background (#F8FAFC).
- **Level 1 (Cards/Containers):** Pure white (#FFFFFF) with a 1px solid border (#E2E8F0) and no shadow.
- **Level 2 (Dropdowns/Modals):** Pure white with a 1px solid border and a "Soft Medical Blur": `0px 4px 20px rgba(15, 82, 186, 0.08)`. The slight blue tint in the shadow reinforces the brand identity.
- **Interaction:** On hover, interactive cards should transition from a flat border to the Level 2 shadow to indicate clickability.

## Shapes

The shape language of this design system is **Rounded**, striking a balance between modern friendliness and professional structure. 

- **Standard Elements:** Buttons, input fields, and tags use a `0.5rem` radius. 
- **Containers:** Large data sections and patient profile cards use `rounded-lg` (1rem) to soften the "clinical" look without appearing unprofessional.
- **Status Indicators:** Small status dots and profile avatars use full `rounded-full` (pill) shapes for immediate recognition.

## Components

- **Buttons:** Primary buttons use the primary blue with white text. Use a 2px horizontal padding multiplier relative to height for a stable, wide appearance.
- **Inputs:** Form fields must have visible 1px borders (#CBD5E1). Active states use a 2px primary blue border with a soft blue focus ring.
- **Chips/Badges:** Use "light" backgrounds for status chips (e.g., a 10% opacity primary blue background with 100% opacity text) to denote categories like "Checked In" or "Urgent."
- **Data Tables:** These are the core of the system. Rows should be 56px high, using alternating subtle grey stripes or 1px dividers. Header cells use `label-bold` typography.
- **Appointment Cards:** Use a left-side color-coded vertical bar (4px width) to indicate the department or urgency at a glance.
- **Search Bar:** Always visible in the top header, featuring a magnifying glass icon and a keyboard shortcut hint (e.g., "Ctrl + K").
- **Alerts:** Use high-contrast banners at the top of the viewport for system-wide notices, using rounded-md shapes and bold iconography.