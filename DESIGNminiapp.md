---
name: Kanghua Medical
colors:
  surface: '#f9f9ff'
  surface-dim: '#d7dae5'
  surface-bright: '#f9f9ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f1f3ff'
  surface-container: '#ebedf9'
  surface-container-high: '#e5e8f3'
  surface-container-highest: '#dfe2ed'
  on-surface: '#181c23'
  on-surface-variant: '#434654'
  inverse-surface: '#2c3039'
  inverse-on-surface: '#eef0fc'
  outline: '#737686'
  outline-variant: '#c3c6d7'
  surface-tint: '#0353da'
  primary: '#003da6'
  on-primary: '#ffffff'
  primary-container: '#0052d9'
  on-primary-container: '#cbd6ff'
  inverse-primary: '#b4c5ff'
  secondary: '#006c46'
  on-secondary: '#ffffff'
  secondary-container: '#84f6bc'
  on-secondary-container: '#00714a'
  tertiary: '#822600'
  on-tertiary: '#ffffff'
  tertiary-container: '#aa3500'
  on-tertiary-container: '#ffccbc'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#dbe1ff'
  primary-fixed-dim: '#b4c5ff'
  on-primary-fixed: '#00174b'
  on-primary-fixed-variant: '#003ea8'
  secondary-fixed: '#87f8be'
  secondary-fixed-dim: '#6adca4'
  on-secondary-fixed: '#002112'
  on-secondary-fixed-variant: '#005234'
  tertiary-fixed: '#ffdbd0'
  tertiary-fixed-dim: '#ffb59c'
  on-tertiary-fixed: '#390c00'
  on-tertiary-fixed-variant: '#832700'
  background: '#f9f9ff'
  on-background: '#181c23'
  surface-variant: '#dfe2ed'
typography:
  h1:
    fontFamily: Public Sans
    fontSize: 24px
    fontWeight: '700'
    lineHeight: 32px
  h2:
    fontFamily: Public Sans
    fontSize: 20px
    fontWeight: '600'
    lineHeight: 28px
  h3:
    fontFamily: Public Sans
    fontSize: 18px
    fontWeight: '600'
    lineHeight: 26px
  body-lg:
    fontFamily: Public Sans
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-md:
    fontFamily: Public Sans
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 22px
  label-sm:
    fontFamily: Public Sans
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
    letterSpacing: 0.02em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  unit: 4px
  container-padding: 16px
  stack-gap: 12px
  element-gap: 8px
  section-margin: 24px
---

## Brand & Style

The brand personality of this design system is rooted in **Reliability, Efficiency, and Compassion**. As a hospital WeChat Mini Program, the interface must bridge the gap between institutional authority and personal care. The UI is designed to feel approachable for all demographics, specifically prioritizing elderly accessibility and high-stress scenarios (e.g., emergency bookings).

The chosen style is **Corporate / Modern**. It leverages ample whitespace to reduce cognitive load and a structured hierarchy to guide patients through complex medical workflows. By combining clinical precision with a soft, card-based layout, the design system fosters a sense of safety and professional calm.

## Colors

The palette is centered around **Trustworthy Blue (#0052D9)**, a color that signifies medical excellence and digital stability. 

- **Primary Blue:** Used for high-emphasis actions, navigation, and brand-defining elements.
- **Secondary Green:** A soft teal-green used for "Success" states and wellness-oriented features (e.g., health reports).
- **Surface & Background:** Clean white (#FFFFFF) is used for cards to suggest hygiene and clarity, while a soft Cool Grey (#F5F7FA) provides a restful background that differentiates the interface from the browser/OS shell.
- **Semantic Colors:** Vivid reds and ambers are reserved strictly for critical alerts or urgent clinic wait times.

## Typography

This design system utilizes **Public Sans** for its exceptional clarity and institutional neutrality. The typographic scale is intentionally generous to accommodate older patients who may struggle with small text.

- **Weight Usage:** Bold weights are used sparingly for section headings and doctor names to ensure quick scanning. Medium weights are used for labels and navigation tabs.
- **Legibility:** Paragraphs utilize a 1.5x line-height ratio to prevent visual crowding.
- **Hierarchy:** High-contrast color usage (Neutral-900 for headings, Neutral-600 for body) ensures that essential information is prioritized.

## Layout & Spacing

The layout follows a **fluid grid** model optimized for mobile viewport dimensions. It utilizes a base 4px/8px rhythm to ensure consistent alignment across all Mini Program components.

- **Margins:** A standard 16px horizontal margin is maintained for all screen edges to ensure content doesn't feel cramped.
- **Gaps:** Vertical spacing between cards is set to 12px or 16px to maintain clear separation of medical departments or services.
- **Safe Areas:** Special consideration is given to bottom navigation and "sticky" action buttons to ensure they remain accessible within the WeChat browser environment.

## Elevation & Depth

Visual hierarchy in this design system is established through **Ambient Shadows** and **Tonal Layers**. This avoids the "flatness" that can make medical apps feel cold, while maintaining a clean, modern aesthetic.

- **Level 1 (Cards):** Soft, low-opacity shadows (0px 4px 12px rgba(0, 0, 0, 0.05)) are used for interactive cards to make them appear "lifted" and tappable.
- **Level 2 (Floating Actions):** Higher elevation (0px 8px 20px rgba(0, 82, 217, 0.12)) is used for primary action buttons like "Register Now" or "Emergency Contact."
- **Layering:** Backgrounds use a light grey tone, while interactive surfaces are pure white to create a clear "layering" effect that guides the eye.

## Shapes

The shape language is defined by **Rounded** geometry (8px to 12px), which softens the traditionally rigid medical environment and makes the digital experience feel more friendly.

- **Standard Elements:** Buttons and small input fields use a 0.5rem (8px) radius.
- **Containers:** Large service cards (e.g., "Department Search") use a 1rem (16px) radius to create a distinct, modern "module" look.
- **Selection States:** Circular or pill-shaped buttons are used for category tags to provide maximum contrast against rectangular cards.

## Components

Components are designed for high touch-accuracy and immediate feedback.

- **Buttons:** Primary buttons are solid Blue (#0052D9) with white text. Secondary buttons use a light blue tint (#E8F2FF) with blue text to indicate lower-priority actions.
- **Medical Cards:** These are white with subtle 1px borders (#E5E6EB) and soft shadows. They include a title, sub-info (e.g., wait time), and a clear chevron icon for navigation.
- **Input Fields:** Large tap targets (min-height 48px) with clearly labeled borders. Focused states are indicated by a 2px primary blue border.
- **Doctor Profiles:** Use circular avatars with a 2px white border and soft shadow to emphasize the human element of care.
- **Quick-Access Chips:** Used for filtering departments (e.g., "Internal Medicine," "Pediatrics") with a pill-shaped design and high-contrast active states.
- **Navigation:** Bottom-tab bar uses simple, thin-stroke icons to maintain the "clean" aesthetic and avoid visual clutter.