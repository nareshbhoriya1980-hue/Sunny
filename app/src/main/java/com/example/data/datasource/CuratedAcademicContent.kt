package com.example.data.datasource

import com.example.data.model.AnimationSceneType
import com.example.data.model.Chapter
import com.example.data.model.ChapterVideoLesson
import com.example.data.model.ExamType
import com.example.data.model.FlashcardItem
import com.example.data.model.FormulaCard
import com.example.data.model.KeyframeMilestone
import com.example.data.model.PracticeQuestion
import com.example.data.model.PyqMarker
import com.example.data.model.Subject
import com.example.data.model.TextbookSection
import com.example.data.model.TrapAlert
import com.example.data.model.VideoScene

object CuratedAcademicContent {

    val chapters: List<Chapter> = listOf(
        // PHYSICS 1: Electrostatics & Capacitance
        Chapter(
            id = "phy_electrostatics",
            name = "Electrostatics & Capacitance",
            subject = Subject.PHYSICS,
            weightage = "8-10% (3-4 Qs High Yield)",
            pyqCount = 42,
            summary = "Fundamental Coulomb's law, Gauss's Theorem, electric potential due to multipoles, capacitance under dielectrics, and energy stored in electrostatic fields.",
            targetExam = ExamType.ALL,
            sections = listOf(
                TextbookSection(
                    id = "phy_es_1",
                    chapterId = "phy_electrostatics",
                    title = "Electric Flux & Gauss's Theorem",
                    subheader = "NCERT Physics Vol 1 - Section 1.15",
                    content = """Electric flux \Phi_E through any closed Gaussian surface enclosing a net charge q_{enclosed} in vacuum is given by the surface integral of the electric field:

\Phi_E = \oint \vec{E} \cdot d\vec{A} = \frac{q_{enclosed}}{\varepsilon_0}

This theorem remains symmetric and valid for any closed geometry, regardless of shape or internal charge distribution. However, evaluating \vec{E} algebraically is practically feasible primarily when high spatial symmetry exists (spherical, cylindrical, or planar).

For an infinite planar thin charge sheet of uniform surface charge density \sigma, the electric field is perpendicular to the sheet on both sides:
\vec{E} = \frac{\sigma}{2\varepsilon_0} \hat{n}

Note that the magnitude of \vec{E} for an infinite sheet is independent of the distance r from the sheet.""",
                    pyqMarker = PyqMarker(
                        examText = "NEET '21, '23 | JEE Main '24",
                        frequencyLabel = "Repeated 5x",
                        yearCount = 5
                    ),
                    trapAlert = TrapAlert(
                        title = "Finite vs Infinite Plate Discrepancy",
                        description = "Do not use E = \\sigma / (2\\varepsilon_0) for a conducting plate! A thick conducting slab has charge on both surfaces, resulting in an external field of E = \\sigma / \\varepsilon_0.",
                        commonMistake = "Mixing up single surface charge sheet with isolated conducting sheet with 2 faces."
                    ),
                    keyDerivation = "Cylindrical Gaussian pillbox spanning cross-sectional area A through a planar sheet: Flux through ends = 2EA. Enclosed charge = \\sigma A. Therefore 2EA = \\sigma A / \\varepsilon_0 \\implies E = \\sigma / (2\\varepsilon_0).",
                    ncertReference = "NCERT Class 12, Chapter 1, Page 39",
                    coreConceptPill = "Surface Integral & Symmetry"
                ),
                TextbookSection(
                    id = "phy_es_2",
                    chapterId = "phy_electrostatics",
                    title = "Parallel Plate Capacitor & Dielectric Insertion",
                    subheader = "NCERT Physics Vol 1 - Section 2.8 & 2.11",
                    content = """A parallel plate capacitor consists of two conducting plates of area A separated by a distance d. In vacuum, capacitance is:
C_0 = \frac{\varepsilon_0 A}{d}

When a dielectric slab of dielectric constant K completely fills the inter-plate volume, the electric polarization reduces internal field to E = E_0 / K, thereby multiplying capacitance by K:
C = K \cdot C_0

Two critical physical scenarios arise frequently in competitive examinations:

1. Battery Connected (Constant Voltage V = V_0):
- Potential difference remains clamped: V = V_0.
- Charge increases: Q = K Q_0.
- Electric field remains constant: E = V/d = E_0.
- Stored electrostatic energy increases: U = \frac{1}{2} C V^2 = K U_0.

2. Battery Disconnected (Isolated System, Constant Charge Q = Q_0):
- Charge remains constant: Q = Q_0.
- Potential difference drops: V = V_0 / K.
- Electric field decreases: E = E_0 / K.
- Stored electrostatic energy decreases: U = \frac{Q^2}{2C} = \frac{U_0}{K}.""",
                    pyqMarker = PyqMarker(
                        examText = "NEET '19, '20, '22 | JEE Main '23",
                        frequencyLabel = "High-Probability Trap",
                        yearCount = 6
                    ),
                    trapAlert = TrapAlert(
                        title = "Work Done by Battery vs External Agent",
                        description = "When dielectric is inserted with battery connected, work done by battery is W_{battery} = \\Delta Q \\cdot V = (K-1) C_0 V^2. The change in stored energy is only \\Delta U = \\frac{1}{2}(K-1) C_0 V^2. The remaining half is dissipated as heat or mechanical work.",
                        commonMistake = "Assuming all battery energy goes into capacitance field storage without heat loss."
                    ),
                    keyDerivation = "Capacitance definition C = Q/V. Gauss's law with polarization vector \\vec{P}: \\oint \\vec{D} \\cdot d\\vec{A} = q_f \\implies D = \\sigma_f = K \\varepsilon_0 E.",
                    ncertReference = "NCERT Class 12, Chapter 2, Page 78",
                    coreConceptPill = "Dielectric Boundary State"
                )
            ),
            videoLesson = ChapterVideoLesson(
                id = "vid_phy_es",
                chapterId = "phy_electrostatics",
                title = "Electrostatics, Gauss Flux & Dielectrics in 3D",
                subtitle = "Animated Concept Journey: High-Precision Field Simulation",
                totalDurationSeconds = 120,
                scenes = listOf(
                    VideoScene(
                        id = "scene_es_1",
                        title = "Electric Flux & Gaussian Surface",
                        subtitle = "Visualizing surface integral of electric field",
                        narration = "Watch how electric field lines emanate radially outwards from enclosed point charges. As the Gaussian cylinder intersects the planar charge sheet, flux penetrates solely through the two circular end caps with area A, proving that E equals sigma over 2 epsilon zero.",
                        animationType = AnimationSceneType.ELECTROSTATIC_FLUX,
                        durationSeconds = 40,
                        visualHighlights = listOf("Radial electric vectors", "Gaussian cylinder end caps", "Surface charge distribution")
                    ),
                    VideoScene(
                        id = "scene_es_2",
                        title = "Dielectric Polarization Under Electric Stress",
                        subtitle = "Molecular Dipoles Aligning Against External Field",
                        narration = "When a dielectric slab slides between the parallel capacitor plates, external electric field tilts molecular electron clouds. Induced bound charges on slab surfaces create an internal counter-field, reducing total electric field to E zero divided by K and amplifying capacitance.",
                        animationType = AnimationSceneType.DIELECTRIC_POLARIZATION,
                        durationSeconds = 45,
                        visualHighlights = listOf("Bound charge sheets", "Electric dipole alignment", "Net capacitance expansion C = K*C0")
                    ),
                    VideoScene(
                        id = "scene_es_3",
                        title = "NEET/JEE High-Frequency Trap: Battery Disconnection",
                        subtitle = "Charge Clamping vs Voltage Clamping",
                        narration = "Notice the critical distinction: When battery remains connected, voltage is locked and charge surges by factor K. But when disconnected, charge is trapped and isolated; potential difference drops by factor K and stored energy decreases.",
                        animationType = AnimationSceneType.DIELECTRIC_POLARIZATION,
                        durationSeconds = 35,
                        visualHighlights = listOf("Battery clamped state: V = V0", "Isolated system: Q = Q0", "Electrostatic energy loss visualization")
                    )
                ),
                keyframes = listOf(
                    KeyframeMilestone(0, "Introduction to Flux", "Gauss's Law applies to any closed geometry"),
                    KeyframeMilestone(40, "Dielectric Insertion", "Polarization vector P reduces internal field"),
                    KeyframeMilestone(85, "Battery Trap Breakdown", "Constant Q vs Constant V exam differentiation")
                ),
                aiPromptUsed = "3D cinematic simulation of electrostatics: glowing blue electric vectors piercing a semi-transparent cylindrical Gaussian surface on an infinite sheet, followed by molecular dipoles aligning inside a parallel plate capacitor.",
                keyTakeaways = listOf(
                    "Flux is determined strictly by interior enclosed charge.",
                    "Dielectric slab multiplies capacitance by factor K.",
                    "Isolated capacitor preserves charge Q, reducing stored energy to U/K."
                )
            )
        ),

        // CHEMISTRY 1: Chemical Equilibrium & Thermodynamics (Class 11)
        Chapter(
            id = "chem_equilibrium",
            name = "Chemical Equilibrium & Le Chatelier",
            subject = Subject.CHEMISTRY,
            classGrade = 11,
            weightage = "6-8% (2-3 Qs High Yield)",
            pyqCount = 38,
            summary = "Dynamic equilibrium, law of mass action, reaction quotient Q vs K, Le Chatelier's stress response, van 't Hoff temperature dependence.",
            targetExam = ExamType.ALL,
            sections = listOf(
                TextbookSection(
                    id = "chem_eq_1",
                    chapterId = "chem_equilibrium",
                    title = "Reaction Quotient (Q) & Equilibrium Constant (K)",
                    subheader = "NCERT Chemistry Vol 1 - Section 7.3",
                    content = """For a generalized reversible gaseous reaction:
aA(g) + bB(g) \rightleftharpoons cC(g) + dD(g)

The relationship between standard Gibbs free energy change \Delta G^\circ and thermodynamic equilibrium constant K is:
\Delta G^\circ = -RT \ln K_{eq}

At non-standard conditions, the instantaneous direction of spontaneity is governed by the comparison between reaction quotient Q and K:
- If Q < K: Forward reaction is spontaneous to produce more products until Q = K.
- If Q = K: Dynamic equilibrium; net rates of forward and reverse reactions are identical.
- If Q > K: Reverse reaction dominates, consuming excess products.

Relation between K_p and K_c:
K_p = K_c (RT)^{\Delta n_g}
where \Delta n_g = (c+d) - (a+b) represents the net change in gaseous stoichiometric moles.""",
                    pyqMarker = PyqMarker(
                        examText = "NEET '20, '22, '24 | JEE Main '23",
                        frequencyLabel = "Core NCERT Line",
                        yearCount = 4
                    ),
                    trapAlert = TrapAlert(
                        title = "Solid & Pure Liquid Activities Trap",
                        description = "Pure solids and pure liquids have chemical activity equal to unity (a = 1). Never incorporate their molar concentrations into K_c or K_p calculations!",
                        commonMistake = "Including CaCO3(s) or CaO(s) in expressions for CaCO3(s) <=> CaO(s) + CO2(g)."
                    ),
                    keyDerivation = "From thermodynamics, \\Delta G = \\Delta G^\\circ + RT \\ln Q. At equilibrium, \\Delta G = 0 and Q = K_{eq} \\implies \\Delta G^\\circ = -RT \\ln K_{eq}.",
                    ncertReference = "NCERT Class 11, Chapter 7, Page 192",
                    coreConceptPill = "Gibbs Energy & Equilibrium"
                ),
                TextbookSection(
                    id = "chem_eq_2",
                    chapterId = "chem_equilibrium",
                    title = "Le Chatelier's Principle & Inert Gas Addition",
                    subheader = "NCERT Chemistry Vol 1 - Section 7.7",
                    content = """If a system in equilibrium is subjected to a change in concentration, temperature, or pressure, the equilibrium position shifts in a direction that tends to counteract that applied change.

Effects of Inert Gas Addition:
1. At Constant Volume (V = \text{const}):
Adding an inert gas (like Helium or Argon) increases total pressure, but does NOT alter the partial pressures or molar concentrations of the reacting gaseous species.
\text{No shift in equilibrium whatsoever!}

2. At Constant Pressure (P = \text{const}):
Adding an inert gas at constant pressure forces the volume of the chamber to increase to maintain pressure. The partial pressures of all components decrease. The system shifts toward the side containing MORE gaseous moles (\Delta n_g > 0).""",
                    pyqMarker = PyqMarker(
                        examText = "NEET '18, '21, '23 | JEE Main '24",
                        frequencyLabel = "High-Frequency PYQ",
                        yearCount = 5
                    ),
                    trapAlert = TrapAlert(
                        title = "Catalyst Does Not Shift Equilibrium Position",
                        description = "A catalyst lowers activation energy equally for both forward and reverse paths. It shortens time to attain equilibrium, but NEVER shifts K_{eq} or the yield of products.",
                        commonMistake = "Thinking catalyst increases product yield in exothermic reactions."
                    ),
                    keyDerivation = "Van 't Hoff equation: \\frac{d \\ln K}{dT} = \\frac{\\Delta H^\\circ}{RT^2}. For endothermic reactions (\\Delta H > 0), K increases with T. For exothermic (\\Delta H < 0), K decreases with T.",
                    ncertReference = "NCERT Class 11, Chapter 7, Page 206",
                    coreConceptPill = "Perturbation Dynamics"
                )
            ),
            videoLesson = ChapterVideoLesson(
                id = "vid_chem_eq",
                chapterId = "chem_equilibrium",
                title = "Equilibrium Kinetics & Le Chatelier Perturbations",
                subtitle = "Microscopic Molecular Dynamics & Real-Time Gas Shifts",
                totalDurationSeconds = 110,
                scenes = listOf(
                    VideoScene(
                        id = "scene_chem_1",
                        title = "Dynamic Equilibrium at the Molecular Scale",
                        subtitle = "Forward Rate Equals Reverse Rate",
                        narration = "In this animated chamber, brown NO2 radicals collide to form colorless N2O4 dimers, while N2O4 continuously dissociates back. Equilibrium is dynamic: reactions never cease, but macroscopic concentrations remain constant.",
                        animationType = AnimationSceneType.EQUILIBRIUM_SHIFT,
                        durationSeconds = 35,
                        visualHighlights = listOf("Brown NO2 gas particles", "Colorless N2O4 dimers", "Dynamic rate equalization")
                    ),
                    VideoScene(
                        id = "scene_chem_2",
                        title = "Piston Compression & Volume Shockwave",
                        subtitle = "System Counteracting Pressure Perturbations",
                        narration = "Watch the piston push downward, slashing volume in half. Instantaneous molecular collision rate quadruples. The system immediately shifts towards the side with fewer gaseous moles (N2O4) to alleviate the increased pressure.",
                        animationType = AnimationSceneType.EQUILIBRIUM_SHIFT,
                        durationSeconds = 40,
                        visualHighlights = listOf("Piston compression", "Concentration spike", "Shift toward fewer moles")
                    ),
                    VideoScene(
                        id = "scene_chem_3",
                        title = "Inert Gas Addition: Constant V vs Constant P",
                        subtitle = "The #1 NEET/JEE Negative Marking Distractor",
                        narration = "Observe Helium atoms injected at constant volume: total pressure rises, but partial pressures of reactants stay unchanged—zero shift! However, at constant pressure, volume expands, shifting the reaction toward more gaseous moles.",
                        animationType = AnimationSceneType.EQUILIBRIUM_SHIFT,
                        durationSeconds = 35,
                        visualHighlights = listOf("Helium inert particles", "Constant volume invariant", "Constant pressure volume expansion")
                    )
                ),
                keyframes = listOf(
                    KeyframeMilestone(0, "Dynamic State", "Rate(forward) = Rate(reverse)"),
                    KeyframeMilestone(35, "Le Chatelier Piston Shift", "Shift toward side with fewer gas moles"),
                    KeyframeMilestone(75, "Inert Gas Trap", "No shift at constant volume!")
                ),
                aiPromptUsed = "Molecular dynamics simulation of NO2 brown gas colliding into N2O4 colorless molecules under piston compression and inert gas helium injection.",
                keyTakeaways = listOf(
                    "Dynamic equilibrium means equal rates, not equal concentrations.",
                    "Pressure increase favors side with fewer gaseous moles.",
                    "Inert gas at constant volume has ZERO effect on equilibrium position."
                )
            )
        ),

        // BIOLOGY 1 (NEET Specialized): Molecular Basis of Inheritance
        Chapter(
            id = "bio_genetics",
            name = "Molecular Basis of Inheritance",
            subject = Subject.BIOLOGY,
            weightage = "12-15% (6-8 Qs High Yield)",
            pyqCount = 64,
            summary = "Structure of polynucleotide chains, Central Dogma, semiconservative replication (Meselson-Stahl), transcription, genetic code, translation, and Lac Operon regulation.",
            targetExam = ExamType.NEET,
            sections = listOf(
                TextbookSection(
                    id = "bio_gen_1",
                    chapterId = "bio_genetics",
                    title = "DNA Replication & Enzyme Complexes",
                    subheader = "NCERT Biology Class 12 - Section 6.4",
                    content = """DNA-dependent DNA polymerase catalyzes the polymerization of deoxynucleotides with extreme fidelity (approximately 2000 bp per second in E. coli). Polymerization strictly proceeds only in the 5' \to 3' direction.

Consequently, at the replication fork:
- Leading Strand: Template strand with polarity 3' \to 5' provides continuous synthesis in 5' \to 3'.
- Lagging Strand: Template strand with polarity 5' \to 3' results in discontinuous synthesis producing Okazaki fragments.

Key Enzymatic Machinery:
1. Helicase: Unwinds the double helix at the origin of replication (ori).
2. Topoisomerase (DNA Gyrase): Relieves supercoiling strain ahead of the replication fork.
3. RNA Primase: Synthesizes a short RNA primer providing a free 3'\text{-OH} group.
4. DNA Ligase: Joins phosphodiester nicks between Okazaki fragments.
5. Deoxyribonucleoside triphosphates (dNTPs): Act as dual-purpose entities: substrate monomers and energy providers (hydrolysis of terminal pyrophosphate bonds drives the reaction).""",
                    pyqMarker = PyqMarker(
                        examText = "NEET '19, '20, '22, '23",
                        frequencyLabel = "Guaranteed 8 Marks",
                        yearCount = 7
                    ),
                    trapAlert = TrapAlert(
                        title = "Direction of Polymerization vs Template Polarity",
                        description = "The template strand is read in 3' \to 5', but nascent daughter strand is synthesized in 5' \to 3'. NEET exam questions deliberately reverse these two polarities in option choices!",
                        commonMistake = "Selecting 3' \\to 5' as the synthesis direction of the new strand."
                    ),
                    keyDerivation = "Meselson and Stahl Experiment (1958): Used heavy nitrogen isotope ^{15}\text{N} and ^{14}\text{N} in CsCl density gradient centrifugation. Generation 1 (20 min) showed hybrid ^{14}\text{N}/^{15}\text{N} density; Generation 2 (40 min) showed equal proportions of hybrid and light DNA.",
                    ncertReference = "NCERT Class 12, Chapter 6, Page 104",
                    coreConceptPill = "Semiconservative Polymerization"
                ),
                TextbookSection(
                    id = "bio_gen_2",
                    chapterId = "bio_genetics",
                    title = "Regulation of Gene Expression: The Lac Operon",
                    subheader = "NCERT Biology Class 12 - Section 6.8",
                    content = """Elucidated by François Jacob and Jacques Monod, the Lac Operon is a polycistronic inducible system regulating lactose catabolism in Escherichia coli.

Structural Genes & Enzymatic Products:
- z gene: Codes for \beta-galactosidase (hydrolyzes lactose into galactose and glucose).
- y gene: Codes for Permease (increases cellular permeability to \beta-galactosides).
- a gene: Codes for Transacetylase.

Regulatory Elements:
- i gene (Constitutive inhibitor): Continuously synthesizes the Lac Repressor protein.
- Operator (o): Sequence bound by active repressor to block RNA polymerase.
- Inducer: Allolactose (or Lactose). Binding of inducer alters repressor conformation, releasing it from the operator.

Regulation Mode: Primarily negative regulation, because expression is turned off by active repressor unless inducer is present.""",
                    pyqMarker = PyqMarker(
                        examText = "NEET '20, '21, '23, '24",
                        frequencyLabel = "Ultra High Yield",
                        yearCount = 8
                    ),
                    trapAlert = TrapAlert(
                        title = "Allolactose vs Lactose as Real Inducer",
                        description = "The physiological inducer is allolactose, formed by low-level transglycosylation catalyzed by \\beta-galactosidase. A basal level of permease and \\beta-galactosidase must always be present inside the cell for lactose to enter.",
                        commonMistake = "Assuming lac operon has zero basal expression in the absent state."
                    ),
                    keyDerivation = "Glucose effect (Catabolite Repression): High intracellular glucose lowers cAMP levels, preventing CAP-cAMP binding to promoter, thereby dampening lac operon expression even if lactose is abundant.",
                    ncertReference = "NCERT Class 12, Chapter 6, Page 116",
                    coreConceptPill = "Inducible Operon Dynamics"
                )
            ),
            videoLesson = ChapterVideoLesson(
                id = "vid_bio_gen",
                chapterId = "bio_genetics",
                title = "DNA Replication Machinery & Lac Operon Animation",
                subtitle = "Nanomachines in 3D Motion: Leading, Lagging & Operon Control",
                totalDurationSeconds = 135,
                scenes = listOf(
                    VideoScene(
                        id = "scene_bio_1",
                        title = "Unzipping the Double Helix",
                        subtitle = "Helicase, Topoisomerase & Replication Fork",
                        narration = "Watch Helicase motor hydrolyze ATP to unzip the parental DNA double helix at 2,000 base pairs per second. DNA Topoisomerase swivels ahead to relieve positive supercoiling strain, keeping the replication bubble stable.",
                        animationType = AnimationSceneType.DNA_REPLICATION_FORK,
                        durationSeconds = 45,
                        visualHighlights = listOf("Hexameric Helicase ring", "Topoisomerase nicking & swiveling", "Single-stranded DNA binding proteins")
                    ),
                    VideoScene(
                        id = "scene_bio_2",
                        title = "Leading vs Discontinuous Lagging Strand",
                        subtitle = "DNA Polymerase 5' to 3' Requirement & Okazaki Fragments",
                        narration = "Because DNA Polymerase strictly synthesizes in the 5' to 3' direction, the leading strand flows continuously towards the fork, while the lagging strand loops back to form Okazaki fragments stitched together by DNA Ligase.",
                        animationType = AnimationSceneType.DNA_REPLICATION_FORK,
                        durationSeconds = 45,
                        visualHighlights = listOf("Continuous leading synthesis", "RNA Primers & Okazaki loops", "DNA Ligase phosphodiester sealing")
                    ),
                    VideoScene(
                        id = "scene_bio_3",
                        title = "Lac Operon: The Allosteric Genetic Switch",
                        subtitle = "Repressor Inactivation by Allolactose",
                        narration = "In the absence of lactose, the active repressor protein sits tightly on the operator, blocking RNA Polymerase. Once allolactose enters, it induces an allosteric shape change that ejects the repressor, triggering polycistronic transcription of z, y, and a genes.",
                        animationType = AnimationSceneType.DNA_REPLICATION_FORK,
                        durationSeconds = 45,
                        visualHighlights = listOf("Active repressor blocking operator", "Allolactose inducer binding", "Polycistronic mRNA transcription")
                    )
                ),
                keyframes = listOf(
                    KeyframeMilestone(0, "Replication Fork Initiation", "Helicase unwinds at ori"),
                    KeyframeMilestone(45, "Okazaki Fragment Synthesis", "DNA Ligase joins phosphodiester bonds"),
                    KeyframeMilestone(90, "Lac Operon Induction", "Allolactose acts as physiological inducer")
                ),
                aiPromptUsed = "Photorealistic 3D medical animation of DNA replication fork: helicase unzipping double helix, DNA polymerase synthesizing Okazaki fragments, and lac repressor protein unbinding operator.",
                keyTakeaways = listOf(
                    "Polymerization is strictly 5' to 3'.",
                    "DNA Ligase seals the nicks between lagging Okazaki fragments.",
                    "Lac operon is primarily under negative regulation with allolactose inducer."
                )
            )
        ),

        // MATHEMATICS 1 (JEE Specialized): Definite Integrals & King's Property
        Chapter(
            id = "math_calculus",
            name = "Definite Integrals & King's Property",
            subject = Subject.MATHEMATICS,
            weightage = "8-10% (3-4 Qs High Yield)",
            pyqCount = 52,
            summary = "Fundamental theorem of calculus, King's symmetry property, periodic functions, reduction formulas, and Leibniz integral rule.",
            targetExam = ExamType.JEE_MAIN,
            sections = listOf(
                TextbookSection(
                    id = "math_calc_1",
                    chapterId = "math_calculus",
                    title = "King's Property & Symmetric Transformations",
                    subheader = "Higher Secondary Calculus - Definite Integrals",
                    content = """The quintessential property utilized in JEE Main and Advanced definite integration is the Reflection/King's Property:

\int_a^b f(x) \, dx = \int_a^b f(a + b - x) \, dx

Special case when lower limit is zero (a=0):
\int_0^a f(x) \, dx = \int_0^a f(a - x) \, dx

Standard Solution Algorithm:
1. Label the initial integral as I.
2. Apply King's property by substituting x \mapsto (a+b-x) to formulate a complementary integral I.
3. Add the two expressions: 2I = \int_a^b [f(x) + f(a+b-x)] \, dx.
4. In high-yield exam problems, f(x) + f(a+b-x) collapses into a constant or cancels the denominator entirely, yielding an immediate algebraic evaluation.""",
                    pyqMarker = PyqMarker(
                        examText = "JEE Main '21, '22, '23, '24 | JEE Adv '23",
                        frequencyLabel = "Guaranteed Question",
                        yearCount = 6
                    ),
                    trapAlert = TrapAlert(
                        title = "Limits of Substitution Trap",
                        description = "When applying King's property, do NOT change the upper and lower limits of integration! Substitution t = a+b-x flips limits, but dt = -dx restores the original orientation.",
                        commonMistake = "Flipping limits to [b, a] and picking negative answer."
                    ),
                    keyDerivation = "Let t = a + b - x \\implies dx = -dt. When x=a \\to t=b, when x=b \\to t=a. Thus \\int_a^b f(x)dx = -\\int_b^a f(a+b-t)dt = \\int_a^b f(a+b-t)dt.",
                    ncertReference = "NCERT Class 12, Chapter 7, Page 341",
                    coreConceptPill = "Integral Symmetry Invariance"
                )
            ),
            videoLesson = ChapterVideoLesson(
                id = "vid_math_calc",
                chapterId = "math_calculus",
                title = "King's Property & Geometric Integral Reflections",
                subtitle = "Visualizing Area Invariance & Cancellation Symmetry in 2D/3D",
                totalDurationSeconds = 100,
                scenes = listOf(
                    VideoScene(
                        id = "scene_math_1",
                        title = "Geometric Area Invariance Under Horizontal Flip",
                        subtitle = "Reflection About Midpoint x = (a+b)/2",
                        narration = "The integral from a to b of f(x) represents the shaded area under the curve. When we transform x to (a+b-x), the curve simply undergoes a horizontal reflection across the midpoint. Because the bounding interval remains [a, b], the enclosed geometric area is identical!",
                        animationType = AnimationSceneType.INTEGRAL_KINGS_PROPERTY,
                        durationSeconds = 35,
                        visualHighlights = listOf("Shaded Riemann area", "Midpoint reflection line", "Congruent area mapping")
                    ),
                    VideoScene(
                        id = "scene_math_2",
                        title = "The Symmetry Collapse: Adding I + I = 2I",
                        subtitle = "Eliminating Complex Trigonometric Denominators",
                        narration = "When integrating square root of sin x over (sqrt sin x + sqrt cos x) from 0 to pi/2, applying King's property flips sin to cos. Adding the original and reflected integrals yields an integrand of exactly 1 everywhere!",
                        animationType = AnimationSceneType.INTEGRAL_KINGS_PROPERTY,
                        durationSeconds = 35,
                        visualHighlights = listOf("Superposition of f(x) and f(a+b-x)", "Denominator cancellation", "Rectangular area simplification: 2I = pi/2 => I = pi/4")
                    ),
                    VideoScene(
                        id = "scene_math_3",
                        title = "Leibniz Differentiation Under Integral Sign",
                        subtitle = "Solving JEE Advanced Definite Calculus Traps",
                        narration = "Watch how differentiating an integral with variable limits d/dx of integral from u(x) to v(x) evaluates to f(v(x)) v'(x) minus f(u(x)) u'(x). This powerful technique resolves difficult limits and differential equations instantaneously.",
                        animationType = AnimationSceneType.INTEGRAL_KINGS_PROPERTY,
                        durationSeconds = 30,
                        visualHighlights = listOf("Moving boundary limits", "Fundamental Theorem of Calculus", "Leibniz chain rule evaluation")
                    )
                ),
                keyframes = listOf(
                    KeyframeMilestone(0, "Geometric Reflection", "Integral f(x)dx = integral f(a+b-x)dx"),
                    KeyframeMilestone(35, "Symmetric Cancellation", "2I trick evaluates trigonometric fractions instantly"),
                    KeyframeMilestone(70, "Leibniz Formula", "Differentiation under the integral sign")
                ),
                aiPromptUsed = "3D dynamic calculus visualization showing function curves reflecting across midpoint lines, Riemann sums consolidating into simple rectangles, and definite integral area conservation.",
                keyTakeaways = listOf(
                    "King's property reflects the curve horizontally without altering enclosed area.",
                    "Adding complementary integrals collapses tough denominators into constant 1.",
                    "Always divide by 2 at the end: 2I = Area => I = Area / 2."
                )
            )
        ),

        // CLASS 10: Light - Reflection and Refraction
        Chapter(
            id = "c10_light_optics",
            name = "Light: Reflection & Refraction",
            subject = Subject.PHYSICS,
            classGrade = 10,
            weightage = "Core Board (7-8 Marks)",
            pyqCount = 35,
            summary = "Spherical mirrors, mirror formula, refractive index, Snell's law, thin lens formula, and power of a lens.",
            targetExam = ExamType.ALL,
            sections = listOf(
                TextbookSection(
                    id = "c10_light_1",
                    chapterId = "c10_light_optics",
                    title = "Spherical Mirrors & Mirror Formula",
                    subheader = "NCERT Class 10 Science - Section 10.2",
                    content = """The relationship between focal length f, object distance u, and image distance v for spherical mirrors:

\frac{1}{f} = \frac{1}{v} + \frac{1}{u}

Linear Magnification produced by mirrors is:
m = \frac{h'}{h} = -\frac{v}{u}

New Cartesian Sign Convention (MANDATORY):
1. All distances are measured from the Pole (P) of the mirror.
2. Distances measured in the direction of incident light are positive (+); against incident light are negative (-).
3. Heights measured upward normal to principal axis are positive (+); downward are negative (-).
4. Focal length of concave mirror is ALWAYS negative (f < 0); convex mirror is ALWAYS positive (f > 0).""",
                    pyqMarker = PyqMarker("CBSE '22, '23, '24", "High Board Frequency", 5),
                    trapAlert = TrapAlert(
                        title = "Magnification Sign Convention",
                        description = "Negative magnification (m < 0) indicates a REAL and INVERTED image. Positive magnification (m > 0) indicates a VIRTUAL and ERECT image. Students frequently mix this up with lens magnification (m = +v/u)!",
                        commonMistake = "Using m = +v/u for spherical mirrors instead of -v/u."
                    ),
                    ncertReference = "NCERT Class 10, Chapter 10, Page 168",
                    coreConceptPill = "Ray Optics Geometry"
                )
            ),
            videoLesson = ChapterVideoLesson(
                id = "vid_c10_light",
                chapterId = "c10_light_optics",
                title = "Light Optics: 3D Ray Tracing & Refraction",
                subtitle = "Watch Light Rays Bend and Form Real & Virtual Images",
                totalDurationSeconds = 105,
                scenes = listOf(
                    VideoScene(
                        id = "scene_c10_1",
                        title = "Spherical Mirror Ray Tracing",
                        subtitle = "Principal Focus & Center of Curvature Rays",
                        narration = "Watch three key rays leave the tip of an object: a parallel ray reflecting through the focus, a focal ray reflecting parallel, and a central ray bouncing straight back. Where they intersect, an inverted real image forms.",
                        animationType = AnimationSceneType.ELECTROSTATIC_FLUX,
                        durationSeconds = 35,
                        visualHighlights = listOf("Incident and reflected rays", "Focus point F", "Real inverted image formation")
                    ),
                    VideoScene(
                        id = "scene_c10_2",
                        title = "Refraction at Glass-Air Boundary: Snell's Law",
                        subtitle = "Bending Toward the Normal in Denser Medium",
                        narration = "As a beam of light travels from rare air into dense glass, its wave front slows down, pivoting the ray towards the normal: n1 sin theta1 equals n2 sin theta2.",
                        animationType = AnimationSceneType.EQUILIBRIUM_SHIFT,
                        durationSeconds = 35,
                        visualHighlights = listOf("Normal boundary line", "Angle of incidence vs refraction", "Speed change in optical medium")
                    ),
                    VideoScene(
                        id = "scene_c10_3",
                        title = "Convex vs Concave Lens Powers",
                        subtitle = "Converging vs Diverging Wavefronts",
                        narration = "A convex lens converges incoming parallel light rays onto the focal point with positive power (P = 1/f in diopters), while a concave lens diverges rays with negative power.",
                        animationType = AnimationSceneType.INTEGRAL_KINGS_PROPERTY,
                        durationSeconds = 35,
                        visualHighlights = listOf("Convex lens convergence", "Diopter focal conversion", "Virtual focal point")
                    )
                ),
                keyframes = listOf(
                    KeyframeMilestone(0, "Mirror Rays", "3 canonical rays determine image location"),
                    KeyframeMilestone(35, "Snell's Law", "n1 sin(i) = n2 sin(r)"),
                    KeyframeMilestone(70, "Lens Power", "Power in Diopters = 1 / f(in meters)")
                ),
                aiPromptUsed = "3D animated ray tracing simulation of light reflecting off concave mirrors and refracting through glass prisms.",
                keyTakeaways = listOf(
                    "Mirror formula: 1/f = 1/v + 1/u, Magnification m = -v/u.",
                    "Focal length is negative for concave, positive for convex.",
                    "Power of a lens is in Dioptres when f is in metres."
                )
            )
        ),

        // CLASS 9: Force and Laws of Motion
        Chapter(
            id = "c9_laws_motion",
            name = "Force & Laws of Motion",
            subject = Subject.PHYSICS,
            classGrade = 9,
            weightage = "Foundational Physics (6-8 Marks)",
            pyqCount = 28,
            summary = "Balanced and unbalanced forces, Newton's 3 laws of motion, inertia and mass, momentum, and conservation of linear momentum.",
            targetExam = ExamType.ALL,
            sections = listOf(
                TextbookSection(
                    id = "c9_motion_1",
                    chapterId = "c9_laws_motion",
                    title = "Newton's Second Law & Momentum",
                    subheader = "NCERT Class 9 Science - Section 9.4",
                    content = """Linear momentum p of an object is defined as the product of its mass m and velocity v:
\vec{p} = m \vec{v}

Newton's Second Law states that the rate of change of momentum is directly proportional to applied unbalanced force:
\vec{F} = \frac{\Delta \vec{p}}{\Delta t} = m \vec{a}

When mass remains constant, Force equals mass multiplied by acceleration (F = ma). SI Unit of force is Newton (1\text{ N} = 1\text{ kg}\cdot\text{m/s}^2).""",
                    pyqMarker = PyqMarker("CBSE Class 9 Exam", "Core Law", 4),
                    trapAlert = TrapAlert(
                        title = "Action-Reaction Acting on Same Body Fallacy",
                        description = "Newton's 3rd Law forces (F_{AB} = -F_{BA}) ALWAYS act on two DIFFERENT interacting bodies! They NEVER cancel each other out on a single object.",
                        commonMistake = "Thinking action and reaction cancel into zero acceleration."
                    ),
                    ncertReference = "NCERT Class 9, Chapter 9, Page 118",
                    coreConceptPill = "Inertia and Force Dynamics"
                )
            ),
            videoLesson = ChapterVideoLesson(
                id = "vid_c9_motion",
                chapterId = "c9_laws_motion",
                title = "Newton's Laws in Action: 3D Force Simulation",
                subtitle = "Inertia, Collision Momentum & Action-Reaction Pairs",
                totalDurationSeconds = 90,
                scenes = listOf(
                    VideoScene(
                        id = "scene_c9_1",
                        title = "Inertia of Rest, Motion & Direction",
                        subtitle = "Why Passengers Lurch Forward When Brakes are Applied",
                        narration = "Inertia is the inherent tendency of an object to resist changes in its state of motion. When a bus suddenly halts, passenger feet stop with the floor while their upper body continues moving forward.",
                        animationType = AnimationSceneType.EQUILIBRIUM_SHIFT,
                        durationSeconds = 30,
                        visualHighlights = listOf("Inertial mass resistance", "Frictionless surface simulation", "First law equilibrium")
                    ),
                    VideoScene(
                        id = "scene_c9_2",
                        title = "Rocket Propulsion & Newton's Third Law",
                        subtitle = "Action and Reaction on Distinct Bodies",
                        narration = "As high-velocity exhaust gases shoot downwards (Action), they impart an equal and opposite upward thrust force on the rocket hull (Reaction), driving it into orbit.",
                        animationType = AnimationSceneType.ELECTROSTATIC_FLUX,
                        durationSeconds = 30,
                        visualHighlights = listOf("Downward gas momentum", "Upward reactive thrust", "Conservation of total momentum")
                    ),
                    VideoScene(
                        id = "scene_c9_3",
                        title = "Collision Impulse & Momentum Conservation",
                        subtitle = "Elastic and Inelastic Kinetic Transfers",
                        narration = "Watch two billiard balls collide: the duration of impact is small, but the impulse exchanged transfers momentum completely, proving that total initial momentum equals total final momentum.",
                        animationType = AnimationSceneType.INTEGRAL_KINGS_PROPERTY,
                        durationSeconds = 30,
                        visualHighlights = listOf("Impulse vector F * dt", "Velocity exchange", "Isolated system zero net external force")
                    )
                ),
                keyframes = listOf(
                    KeyframeMilestone(0, "First Law", "Objects stay at rest unless compelled by force"),
                    KeyframeMilestone(30, "Second Law", "F = dp/dt = m*a"),
                    KeyframeMilestone(60, "Third Law", "Every action has an equal and opposite reaction")
                ),
                aiPromptUsed = "3D physics simulation of colliding carts, rocket exhaust thrust, and passenger inertia in decelerating vehicles.",
                keyTakeaways = listOf(
                    "Mass is the quantitative measure of inertia.",
                    "F = m*a is derived from rate of change of momentum.",
                    "Action and reaction forces act on different bodies simultaneously."
                )
            )
        ),

        // CLASS 8: Force and Pressure & Sound
        Chapter(
            id = "c8_force_sound",
            name = "Force, Pressure & Sound Waves",
            subject = Subject.PHYSICS,
            classGrade = 8,
            weightage = "Middle School Foundation",
            pyqCount = 20,
            summary = "Contact and non-contact forces, pressure as force per unit area, atmospheric pressure, and production of sound through vibrating bodies.",
            targetExam = ExamType.ALL,
            sections = listOf(
                TextbookSection(
                    id = "c8_p_1",
                    chapterId = "c8_force_sound",
                    title = "Pressure & Surface Area Dependence",
                    subheader = "NCERT Class 8 Science - Chapter 11",
                    content = """Pressure is the force acting perpendicular to a unit surface area:

\text{Pressure } (P) = \frac{\text{Force } (F)}{\text{Area } (A)}

Key Deductions:
1. For the same applied force, a smaller contact area exerts a vastly greater pressure.
2. That is why a sharp knife cuts vegetables effortlessly compared to a blunt knife, and nails have pointed tips.
3. SI unit of pressure is Pascal (\text{Pa} = \text{N/m}^2).

Sound Waves:
Sound is produced by vibrating objects. It propagates as longitudinal waves requiring a material medium (solid, liquid, or gas). Sound CANNOT travel in a vacuum!""",
                    pyqMarker = PyqMarker("Class 8 Exam", "Foundation", 3),
                    trapAlert = TrapAlert(
                        title = "Sound in Vacuum Trap",
                        description = "Unlike light which travels through vacuum, sound is a mechanical wave requiring molecular collisions. In outer space or a vacuum bell jar, sound volume drops to absolute zero.",
                        commonMistake = "Assuming astronauts can hear each other directly without radio headsets."
                    ),
                    ncertReference = "NCERT Class 8, Chapter 11 & 13",
                    coreConceptPill = "Area Inverse Law & Vibration"
                )
            ),
            videoLesson = ChapterVideoLesson(
                id = "vid_c8_sound",
                chapterId = "c8_force_sound",
                title = "Vibrations, Pressure & Sound Waves in 3D",
                subtitle = "Visualizing Compression, Rarefaction & Hydraulic Lift",
                totalDurationSeconds = 85,
                scenes = listOf(
                    VideoScene(
                        id = "scene_c8_1",
                        title = "Sharp Edge vs Broad Surface Pressure",
                        subtitle = "P = F/A Explained with Everyday Tools",
                        narration = "Watch how a pointed nail concentrates force onto a fraction of a millimeter, multiplying pressure thousands of times to pierce hard wood easily.",
                        animationType = AnimationSceneType.EQUILIBRIUM_SHIFT,
                        durationSeconds = 25,
                        visualHighlights = listOf("Area reduction", "Pressure spike", "Atmospheric column height")
                    ),
                    VideoScene(
                        id = "scene_c8_2",
                        title = "Vibrating Tuning Fork & Air Wavefronts",
                        subtitle = "Compressions and Rarefactions in Motion",
                        narration = "As tuning fork prongs vibrate, they push air molecules together into dense compressions, alternating with spread-out rarefactions that travel into your ear drum.",
                        animationType = AnimationSceneType.DNA_REPLICATION_FORK,
                        durationSeconds = 30,
                        visualHighlights = listOf("Tuning fork vibration", "Air particle density waves", "Ear drum oscillation")
                    ),
                    VideoScene(
                        id = "scene_c8_3",
                        title = "Pitch vs Loudness: Amplitude & Frequency",
                        subtitle = "High Pitch Whistle vs Deep Loud Drum",
                        narration = "Amplitude determines loudness: bigger vibrations create louder sound. Frequency (vibrations per second in Hertz) determines pitch: faster vibrations make squeaky high-pitched tones.",
                        animationType = AnimationSceneType.INTEGRAL_KINGS_PROPERTY,
                        durationSeconds = 30,
                        visualHighlights = listOf("Wave amplitude height", "Frequency oscillation speed", "Hertz measurement")
                    )
                ),
                keyframes = listOf(
                    KeyframeMilestone(0, "Pressure Formula", "P = F / A in Pascals"),
                    KeyframeMilestone(25, "Sound Transmission", "Requires medium; zero in vacuum"),
                    KeyframeMilestone(55, "Amplitude & Pitch", "Amplitude = Loudness, Frequency = Pitch")
                ),
                aiPromptUsed = "3D visual explanation of air compressions produced by vibrating vocal cords and ear drum mechanics.",
                keyTakeaways = listOf(
                    "Pressure is inversely proportional to contact area.",
                    "Sound requires a material medium to propagate.",
                    "Loudness depends on amplitude; pitch depends on frequency."
                )
            )
        ),

        // CLASS 5-7: States of Matter & Nutrition
        Chapter(
            id = "c5_matter_life",
            name = "States of Matter & Nutrition in Living Things",
            subject = Subject.CHEMISTRY,
            classGrade = 5,
            weightage = "Junior Science Foundation",
            pyqCount = 15,
            summary = "Solids, liquids, gases, melting, evaporation, condensation, water cycle, and balanced diet nutrition for young minds.",
            targetExam = ExamType.ALL,
            sections = listOf(
                TextbookSection(
                    id = "c5_matter_1",
                    chapterId = "c5_matter_life",
                    title = "Three States of Matter & Particle Arrangement",
                    subheader = "Primary School Science Curriculum",
                    content = """Everything around us is made of matter. Matter exists in three primary states:

1. Solid: Particles are tightly packed in rigid, orderly arrangements. Solids have a definite shape and fixed volume.
2. Liquid: Particles are close but can slide past one another. Liquids take the shape of their container with a fixed volume.
3. Gas: Particles are far apart and move rapidly in all directions. Gases fill any available space with no fixed shape or volume.

Changes of State:
- Heating a solid turns it into liquid (Melting).
- Heating liquid turns it into gas (Evaporation).
- Cooling gas turns it into liquid (Condensation).
- Cooling liquid turns it into solid (Freezing).""",
                    pyqMarker = PyqMarker("Junior Olympiad", "Foundational", 3),
                    trapAlert = TrapAlert(
                        title = "Steam vs Water Temperature Trap",
                        description = "Steam at 100°C causes more severe burns than boiling water at 100°C because steam carries extra latent heat of vaporization!",
                        commonMistake = "Assuming steam and boiling water deliver the same heat energy."
                    ),
                    ncertReference = "Class 5-6 Science NCERT",
                    coreConceptPill = "Molecular Motion"
                )
            ),
            videoLesson = ChapterVideoLesson(
                id = "vid_c5_water",
                chapterId = "c5_matter_life",
                title = "States of Matter & The Water Cycle in 3D",
                subtitle = "Fun Animated Molecule Dance: Ice, Water & Cloud Formation",
                totalDurationSeconds = 75,
                scenes = listOf(
                    VideoScene(
                        id = "scene_c5_1",
                        title = "Meet the Water Molecules!",
                        subtitle = "Tightly Packed Ice to Bouncing Steam",
                        narration = "In an ice cube, water molecules hold hands tightly and vibrate in place. When warmed by the sun, they start dancing and flowing as liquid water, and finally evaporate into high-flying vapor clouds!",
                        animationType = AnimationSceneType.EQUILIBRIUM_SHIFT,
                        durationSeconds = 25,
                        visualHighlights = listOf("Crystal ice lattice", "Flowing liquid water", "Bouncing vapor particles")
                    ),
                    VideoScene(
                        id = "scene_c5_2",
                        title = "The Great Water Cycle Adventure",
                        subtitle = "Evaporation, Condensation & Rain",
                        narration = "Watch ocean water heat up, rising into the cold upper atmosphere where it condenses into fluffy rainclouds, then precipitates down into mountain streams.",
                        animationType = AnimationSceneType.ELECTROSTATIC_FLUX,
                        durationSeconds = 25,
                        visualHighlights = listOf("Solar evaporation", "Cloud droplet coalescence", "Rain precipitation")
                    ),
                    VideoScene(
                        id = "scene_c5_3",
                        title = "Balanced Diet & Healthy Nutrition",
                        subtitle = "Carbohydrates, Proteins, Fats & Vitamins",
                        narration = "Our bodies need carbs for instant energy, proteins for muscle building, healthy fats for warmth, and vitamins and minerals to fight off diseases.",
                        animationType = AnimationSceneType.DNA_REPLICATION_FORK,
                        durationSeconds = 25,
                        visualHighlights = listOf("Nutrient food pyramid", "Energy release", "Immune defense cells")
                    )
                ),
                keyframes = listOf(
                    KeyframeMilestone(0, "3 States", "Solids, Liquids, and Gases"),
                    KeyframeMilestone(25, "Water Cycle", "Evaporation -> Condensation -> Rain"),
                    KeyframeMilestone(50, "Nutrition", "Balanced diet keeps our body strong")
                ),
                aiPromptUsed = "Colorful friendly 3D cartoon animation of dancing water molecules transforming from ice to water droplets and clouds.",
                keyTakeaways = listOf(
                    "Matter has 3 states: solid, liquid, gas.",
                    "Heating causes melting & evaporation; cooling causes condensation & freezing.",
                    "A balanced diet provides all nutrients in proper proportions."
                )
            )
        )
    ,

        // CLASS 11: Thermodynamics & Heat Engines
        Chapter(
            id = "c11_thermodynamics",
            name = "Thermodynamics & Heat Engines",
            subject = Subject.PHYSICS,
            classGrade = 11,
            weightage = "8-9% (Core NEET & JEE)",
            pyqCount = 42,
            summary = "First Law of thermodynamics, P-V indicator diagrams, isothermal and adiabatic expansions, Carnot heat engine, second law.",
            targetExam = ExamType.ALL,
            sections = listOf(
                TextbookSection(
                    id = "c11_th_1",
                    chapterId = "c11_thermodynamics",
                    title = "First Law of Thermodynamics & P-V Indicator Work",
                    subheader = "NCERT Physics Class 11 - Chapter 12",
                    content = """The First Law of Thermodynamics is the principle of conservation of energy applied to thermodynamic systems:
\Delta Q = \Delta U + \Delta W

Where:
- \Delta Q is heat transferred to the system.
- \Delta U is change in internal energy (for an ideal gas, U depends ONLY on temperature T: \Delta U = n C_v \Delta T).
- \Delta W = \int P dV is work done by the system.

On an indicator P-V diagram:
- The work done during any process is equal to the area under the P-V curve.
- For a cyclic process returning to its initial state, net work done is the enclosed loop area (positive if clockwise, negative if counter-clockwise).""",
                    pyqMarker = PyqMarker("NEET '21, '23 | JEE Main '24", "High Yield", 5),
                    trapAlert = TrapAlert(
                        title = "Sign Convention for Work in Physics vs Chemistry",
                        description = "In physics, work done BY gas on surroundings during expansion is POSITIVE (\\Delta W = +P\\Delta V). In chemistry, IUPAC sets work done ON gas as positive (\\Delta W = -P_{ext}\\Delta V).",
                        commonMistake = "Mixing up delta W sign conventions between physics and chemistry exam papers."
                    ),
                    ncertReference = "NCERT Class 11 Physics, Chapter 12, Page 305",
                    coreConceptPill = "Conservation of Energy"
                ),
                TextbookSection(
                    id = "c11_th_2",
                    chapterId = "c11_thermodynamics",
                    title = "Carnot Heat Engine & Maximum Theoretical Efficiency",
                    subheader = "NCERT Physics Class 11 - Section 12.11",
                    content = """A Carnot engine is an ideal reversible heat engine operating between two thermal reservoirs: a hot source at T_1 and a cold sink at T_2.

The Carnot cycle consists of four successive reversible steps:
1. Isothermal expansion at source temperature T_1 (absorbing heat Q_1).
2. Adiabatic expansion (temperature falls from T_1 to T_2).
3. Isothermal compression at sink temperature T_2 (rejecting heat Q_2).
4. Adiabatic compression (temperature rises from T_2 back to T_1).

Thermal efficiency:
\eta = 1 - \frac{Q_2}{Q_1} = 1 - \frac{T_2}{T_1}

No real engine can have efficiency higher than a Carnot engine operating between the same two temperature reservoirs.""",
                    pyqMarker = PyqMarker("NEET '19, '22 | JEE Main '23", "Formula Direct", 4),
                    trapAlert = TrapAlert(
                        title = "Kelvin Temperature Requirement Trap",
                        description = "Temperatures T_1 and T_2 MUST strictly be substituted in KELVIN (K = °C + 273.15). Using Celsius values in the efficiency ratio produces disastrous errors!",
                        commonMistake = "Calculating eta using Celsius temperatures directly."
                    ),
                    ncertReference = "NCERT Class 11 Physics, Chapter 12, Page 312",
                    coreConceptPill = "Reversible Cycle"
                )
            ),
            videoLesson = ChapterVideoLesson(
                id = "vid_c11_th",
                chapterId = "c11_thermodynamics",
                title = "Thermodynamic Cycles & Carnot Dynamics in 3D",
                subtitle = "Visualizing P-V Indicator Area & Molecular Gas Kinetic Strokes",
                totalDurationSeconds = 100,
                scenes = listOf(
                    VideoScene(
                        id = "scene_c11_1",
                        title = "P-V Indicator Diagram & Work Enclosed",
                        subtitle = "Area Inside Clockwise Loop Equals Net Output Work",
                        narration = "Watch how expanding gas sweeps out area beneath the upper isothermal curve, performing work on the piston. The enclosed loop area directly equals the net mechanical work delivered per cycle.",
                        animationType = AnimationSceneType.INTEGRAL_KINGS_PROPERTY,
                        durationSeconds = 35,
                        visualHighlights = listOf("P-V curve traversal", "Integration area shading", "Net work calculation")
                    ),
                    VideoScene(
                        id = "scene_c11_2",
                        title = "Four Strokes of the Carnot Cycle",
                        subtitle = "Isothermal Absorption to Adiabatic Compression",
                        narration = "Follow the working substance through its four reversible strokes: heat intake at high temperature, adiabatic expansion doing work, heat rejection to the cold sink, and adiabatic recompression.",
                        animationType = AnimationSceneType.EQUILIBRIUM_SHIFT,
                        durationSeconds = 35,
                        visualHighlights = listOf("Hot reservoir contact", "Insulated adiabatic chamber", "Heat rejection flow")
                    ),
                    VideoScene(
                        id = "scene_c11_3",
                        title = "Isothermal vs Adiabatic Slope Comparison",
                        subtitle = "Adiabatic Curve is Gamma Times Steeper",
                        narration = "Notice how the adiabatic curve plunges more steeply than the isothermal curve by a factor of gamma, because no heat enters to replenish the kinetic energy consumed by gas work.",
                        animationType = AnimationSceneType.CUSTOM_AI_SIMULATION,
                        durationSeconds = 30,
                        visualHighlights = listOf("Slope dP/dV comparison", "Gamma factor ratio", "Temperature drop curve")
                    )
                ),
                keyframes = listOf(
                    KeyframeMilestone(0, "First Law", "dQ = dU + dW"),
                    KeyframeMilestone(35, "Carnot Efficiency", "eta = 1 - (T2/T1) in Kelvin"),
                    KeyframeMilestone(70, "Adiabatic Slope", "dP/dV = -gamma (P/V)")
                ),
                aiPromptUsed = "3D animated simulation of a gas piston executing a four-stroke Carnot cycle alongside an interactive synchronized P-V indicator diagram.",
                keyTakeaways = listOf(
                    "First Law: Heat supplied goes into internal energy and mechanical work.",
                    "Carnot efficiency depends solely on absolute temperatures of source and sink.",
                    "Work done in cyclic processes equals the enclosed loop area on the P-V diagram."
                )
            )
        ),

        // CLASS 7: Heat, Temperature, Acids & Bases
        Chapter(
            id = "c7_heat_acids",
            name = "Heat, Temperature, Acids & Bases",
            subject = Subject.CHEMISTRY,
            classGrade = 7,
            weightage = "Middle School Foundation",
            pyqCount = 20,
            summary = "Temperature measurement, conduction, convection, radiation, indicators, acids, bases, and neutralization reactions.",
            targetExam = ExamType.ALL,
            sections = listOf(
                TextbookSection(
                    id = "c7_ha_1",
                    chapterId = "c7_heat_acids",
                    title = "Heat Transfer: Conduction, Convection & Radiation",
                    subheader = "NCERT Science Class 7 - Chapter 4",
                    content = """Heat is a form of energy that flows spontaneously from a body at higher temperature to one at lower temperature.

Three primary modes of heat transfer:
1. Conduction: Transfer of heat through solid matter without macroscopic movement of the material itself. High-energy atoms vibrate against neighboring atoms. Metals are good conductors.
2. Convection: Transfer of heat in fluids (liquids and gases) via actual upward movement of warmer, lighter fluid columns replaced by sinking cold fluid (e.g. Sea Breeze and Land Breeze).
3. Radiation: Transfer of heat via electromagnetic waves that requires NO material medium (e.g. Solar radiation reaching Earth across interplanetary vacuum).""",
                    pyqMarker = PyqMarker("Class 7 Science Olympiad", "Core Concepts", 3),
                    trapAlert = TrapAlert(
                        title = "Vacuum Heat Transfer Trap",
                        description = "Neither conduction nor convection can take place in a vacuum because both require physical particles. Solar warmth reaches us exclusively through thermal radiation!",
                        commonMistake = "Assuming heat cannot travel through empty space."
                    ),
                    ncertReference = "NCERT Class 7 Science, Chapter 4, Page 39",
                    coreConceptPill = "Thermal Transport"
                ),
                TextbookSection(
                    id = "c7_ha_2",
                    chapterId = "c7_heat_acids",
                    title = "Acids, Bases & Neutralization Reactions",
                    subheader = "NCERT Science Class 7 - Chapter 5",
                    content = """Acids taste sour and turn blue litmus paper red (e.g. Hydrochloric acid HCl in stomach, Acetic acid in vinegar, Citric acid in lemons).
Bases taste bitter, feel slippery/soapy, and turn red litmus blue (e.g. Sodium hydroxide NaOH, Calcium hydroxide in lime water).

Indicators:
- Litmus (extracted from lichens): Red in acid, Blue in base.
- Turmeric: Stays yellow in acid, turns deep reddish-brown in base.
- Phenolphthalein: Colorless in acid/neutral, vibrant pink in base.

Neutralization Reaction:
When an acid and base react in stoichiometric quantities, they neutralize each other's effects, producing salt, water, and heat:
\text{Acid} + \text{Base} \rightarrow \text{Salt} + \text{Water} + \text{Heat}""",
                    pyqMarker = PyqMarker("Junior Science Olympiad", "High Frequency", 4),
                    trapAlert = TrapAlert(
                        title = "Dilution Safety Rule Trap",
                        description = "Always add acid slowly to water down the side of the container with continuous stirring. NEVER pour water into concentrated acid, or the extreme local boiling will splash caustic acid into your face!",
                        commonMistake = "Adding water into acid."
                    ),
                    ncertReference = "NCERT Class 7 Science, Chapter 5, Page 50",
                    coreConceptPill = "Neutralization"
                )
            ),
            videoLesson = ChapterVideoLesson(
                id = "vid_c7_ha",
                chapterId = "c7_heat_acids",
                title = "Heat Flow & Acid-Base Color Transitions in 3D",
                subtitle = "Convection Currents & Litmus Indicator Color Shifts",
                totalDurationSeconds = 85,
                scenes = listOf(
                    VideoScene(
                        id = "scene_c7_1",
                        title = "Conduction in Metal Rod & Convection Sea Breeze",
                        subtitle = "Vibrating Atoms and Warm Air Rising",
                        narration = "Watch thermal vibration travel atom-to-atom down a copper rod melting wax pins. Next, see land warm up faster by day, creating rising convection currents that pull in cool ocean breezes.",
                        animationType = AnimationSceneType.EQUILIBRIUM_SHIFT,
                        durationSeconds = 45,
                        visualHighlights = listOf("Atomic lattice vibration", "Convection loop circulation", "Thermal radiation emission")
                    ),
                    VideoScene(
                        id = "scene_c7_2",
                        title = "Acid Base Neutralization & Salt Synthesis",
                        subtitle = "Litmus Turning Color: Red to Blue and Pink Phenolphthalein",
                        narration = "Hydrochloric acid meets sodium hydroxide! Hydrogen ions snap together with hydroxide ions to form pure water, leaving dissolved sodium chloride table salt while releasing energetic heat.",
                        animationType = AnimationSceneType.ELECTROSTATIC_FLUX,
                        durationSeconds = 40,
                        visualHighlights = listOf("H+ and OH- ionic bonding", "Water molecule synthesis", "Exothermic heat release")
                    )
                ),
                keyframes = listOf(
                    KeyframeMilestone(0, "3 Heat Modes", "Conduction, Convection, Radiation"),
                    KeyframeMilestone(45, "Indicators", "Litmus: Blue to Red in Acid, Red to Blue in Base"),
                    KeyframeMilestone(70, "Neutralization", "Acid + Base -> Salt + Water + Heat")
                ),
                aiPromptUsed = "3D visual animation showing molecular conduction in metals, warm fluid convection currents, and ionic neutralization between acid and alkali.",
                keyTakeaways = listOf(
                    "Conduction requires solids; convection occurs in fluids; radiation needs no medium.",
                    "Acids turn blue litmus red; bases turn red litmus blue.",
                    "Neutralization generates salt, water, and heat."
                )
            )
        ),

        // CLASS 6: Components of Food, Light & Shadows
        Chapter(
            id = "c6_food_light",
            name = "Components of Food, Light & Shadows",
            subject = Subject.PHYSICS,
            classGrade = 6,
            weightage = "Middle School Foundation",
            pyqCount = 18,
            summary = "Essential food nutrients, starch iodine test, rectilinear propagation of light, shadows, transparent/translucent/opaque materials, and pinhole camera.",
            targetExam = ExamType.ALL,
            sections = listOf(
                TextbookSection(
                    id = "c6_fl_1",
                    chapterId = "c6_food_light",
                    title = "Light Travels in Straight Lines: Shadows & Pinhole Cameras",
                    subheader = "NCERT Science Class 6 - Chapter 11",
                    content = """Light travels along straight-line paths (Rectilinear propagation of light).

Optical properties of matter:
- Transparent: Allows light to pass through completely (clear water, glass).
- Translucent: Allows light to pass partially; objects appear blurred (frosted glass, butter paper).
- Opaque: Does not allow any light to pass, casting dark shadows behind it (wood, stone, metal).

Pinhole Camera:
A simple pinhole camera requires no lens. Light rays from the top and bottom of an illuminated object travel in straight lines through the tiny pinhole and strike the opposite screen:
- The image formed is REAL and INVERTED (upside down).
- Internal colors and features of the object are preserved, unlike shadows which are always silhouette dark!""",
                    pyqMarker = PyqMarker("Class 6 Science Olympiad", "Foundational", 3),
                    trapAlert = TrapAlert(
                        title = "Shadow vs Pinhole Image Distinction",
                        description = "A shadow is formed when an opaque object blocks light—it shows only shape/outline and is completely black. A pinhole camera image preserves the colors and features of the object, but flipped upside down!",
                        commonMistake = "Confusing shadows with real inverted images."
                    ),
                    ncertReference = "NCERT Class 6 Science, Chapter 11, Page 108",
                    coreConceptPill = "Rectilinear Propagation"
                ),
                TextbookSection(
                    id = "c6_fl_2",
                    chapterId = "c6_food_light",
                    title = "Major Nutrients & The Starch Iodine Test",
                    subheader = "NCERT Science Class 6 - Chapter 2",
                    content = """Our food consists of five major nutrient groups:
1. Carbohydrates: Main energy source (starches and sugars).
2. Fats: Stored energy reserves (provide more energy per gram than carbs).
3. Proteins: Body-building and repair of damaged tissues.
4. Vitamins & Minerals: Protective foods that boost immunity.
5. Dietary Fibre (Roughage) & Water: Essential for digestive motility.

Testing for Starch:
Add 2-3 drops of dilute iodine solution to the food sample.
- If the food contains starch, it turns deep BLUE-BLACK.
- A raw cut potato instantly turns blue-black due to its high starch concentration.""",
                    pyqMarker = PyqMarker("Junior Olympiad", "Lab Practical", 2),
                    trapAlert = TrapAlert(
                        title = "Protein vs Starch Test Reagent Trap",
                        description = "Do not use iodine for protein testing! Protein testing requires Copper Sulphate (CuSO4) + Caustic Soda (NaOH), which turns violet. Iodine is strictly for starch.",
                        commonMistake = "Using iodine to test for protein."
                    ),
                    ncertReference = "NCERT Class 6 Science, Chapter 2, Page 12",
                    coreConceptPill = "Nutrient Chemistry"
                )
            ),
            videoLesson = ChapterVideoLesson(
                id = "vid_c6_fl",
                chapterId = "c6_food_light",
                title = "Light Rays, Shadows & Pinhole Cameras in 3D",
                subtitle = "Straight Line Rays Inverting Images & Starch Molecular Color Magic",
                totalDurationSeconds = 75,
                scenes = listOf(
                    VideoScene(
                        id = "scene_c6_1",
                        title = "Straight Line Ray Tracing through a Pinhole",
                        subtitle = "Top Ray Goes Down, Bottom Ray Goes Up",
                        narration = "Watch straight light rays emanate from a bright red flower. The top ray travels through the tiny hole to hit the bottom of the screen, while the bottom ray hits the top, proving why pinhole images are flipped upside down!",
                        animationType = AnimationSceneType.ELECTROSTATIC_FLUX,
                        durationSeconds = 40,
                        visualHighlights = listOf("Straight ray paths", "Pinhole aperture intersection", "Upside down inverted image")
                    ),
                    VideoScene(
                        id = "scene_c6_2",
                        title = "Starch Iodine Test Color Reaction",
                        subtitle = "Iodine Trapped in Starch Helices Turns Deep Blue-Black",
                        narration = "When drops of brown iodine hit potato slices, iodine molecules slip directly inside the coiled helical chains of starch, changing absorbed light wavelengths to vivid blue-black.",
                        animationType = AnimationSceneType.DNA_REPLICATION_FORK,
                        durationSeconds = 35,
                        visualHighlights = listOf("Brown iodine dropper", "Helical starch polymer", "Blue-black color change")
                    )
                ),
                keyframes = listOf(
                    KeyframeMilestone(0, "Light Property", "Light travels in straight lines"),
                    KeyframeMilestone(40, "Pinhole Image", "Real and Inverted"),
                    KeyframeMilestone(60, "Iodine Test", "Turns Blue-Black with Starch")
                ),
                aiPromptUsed = "3D educational ray tracing showing straight light rays passing through a dark cardboard pinhole box forming an upside down image.",
                keyTakeaways = listOf(
                    "Light travels in straight lines (rectilinear propagation).",
                    "Pinhole camera produces a real, inverted image with true colors.",
                    "Iodine turns blue-black in the presence of starch."
                )
            )
        )

    )

    val formulas: List<FormulaCard> = listOf(
        FormulaCard(
            id = "f_gauss",
            name = "Gauss's Law for Electrostatics",
            latexFormula = "\\Phi = \\oint \\vec{E} \\cdot d\\vec{A} = \\frac{Q_{enclosed}}{\\varepsilon_0}",
            subject = Subject.PHYSICS,
            chapterName = "Electrostatics",
            variables = listOf(
                "\\Phi : \\text{Electric flux}",
                "\\vec{E} : \\text{Electric field vector}",
                "d\\vec{A} : \\text{Differential area vector}",
                "Q_{enclosed} : \\text{Net interior charge}",
                "\\varepsilon_0 : \\text{Permittivity of free space} \\approx 8.854 \\times 10^{-12} \\text{ F/m}"
            ),
            siUnits = "\\text{N} \\cdot \\text{m}^2 / \\text{C} \\text{ or } \\text{V} \\cdot \\text{m}",
            dimensionalFormula = "[M L^3 T^{-3} A^{-1}]",
            crucialCaveats = "Flux depends ONLY on charges enclosed within the Gaussian surface. External charges affect local electric field \\vec{E} at every boundary point, but contribute net zero to total closed flux integral."
        ),
        FormulaCard(
            id = "f_capacitance_energy",
            name = "Electrostatic Energy in Capacitor",
            latexFormula = "U = \\frac{1}{2} C V^2 = \\frac{Q^2}{2C} = \\frac{1}{2} Q V",
            subject = Subject.PHYSICS,
            chapterName = "Capacitance",
            variables = listOf(
                "U : \\text{Stored electrostatic potential energy}",
                "C : \\text{Capacitance in Farads}",
                "V : \\text{Potential difference across plates}",
                "Q : \\text{Stored charge magnitude}"
            ),
            siUnits = "\\text{Joules (J)}",
            dimensionalFormula = "[M L^2 T^{-2}]",
            crucialCaveats = "Energy density in electrostatic field is u = \\frac{1}{2} \\varepsilon_0 E^2. When connecting two charged capacitors in parallel, charge is conserved, but energy is ALWAYS dissipated as heat or EM radiation."
        ),
        FormulaCard(
            id = "f_gibbs_equilibrium",
            name = "Standard Gibbs Energy & Equilibrium",
            latexFormula = "\\Delta G^\\circ = - R T \\ln K_{eq} = - 2.303 R T \\log_{10} K_{eq}",
            subject = Subject.CHEMISTRY,
            chapterName = "Chemical Equilibrium",
            variables = listOf(
                "\\Delta G^\\circ : \\text{Standard Gibbs free energy change}",
                "R : \\text{Universal gas constant} \\approx 8.314 \\text{ J/(mol K)}",
                "T : \\text{Absolute temperature in Kelvin}",
                "K_{eq} : \\text{Thermodynamic equilibrium constant}"
            ),
            siUnits = "\\text{J/mol} \\text{ or } \\text{kJ/mol}",
            dimensionalFormula = "[M L^2 T^{-2} N^{-1}]",
            crucialCaveats = "If \\Delta G^\\circ < 0 \\implies K > 1 (products favored at standard state). If \\Delta G^\\circ > 0 \\implies K < 1. Note: \\Delta G at equilibrium is ALWAYS zero, whereas \\Delta G^\\circ is a non-zero thermodynamic constant."
        ),
        FormulaCard(
            id = "f_arrhenius",
            name = "Arrhenius Rate Equation",
            latexFormula = "k = A e^{-E_a / (RT)} \\implies \\ln \\left(\\frac{k_2}{k_1}\\right) = \\frac{E_a}{R} \\left( \\frac{1}{T_1} - \\frac{1}{T_2} \\right)",
            subject = Subject.CHEMISTRY,
            chapterName = "Chemical Kinetics",
            variables = listOf(
                "k : \\text{Rate constant}",
                "A : \\text{Pre-exponential frequency factor}",
                "E_a : \\text{Activation energy in J/mol}",
                "R : \\text{Gas constant}",
                "T : \\text{Temperature in Kelvin}"
            ),
            siUnits = "\\text{Depends on reaction order; } E_a \\text{ in J/mol}",
            dimensionalFormula = "E_a : [M L^2 T^{-2} N^{-1}]",
            crucialCaveats = "Activation energy E_a is temperature-independent in classical Arrhenius theory. Catalyst reduces E_a, thereby accelerating rate k exponentially without altering \\Delta H of the reaction."
        ),
        FormulaCard(
            id = "f_kings_property",
            name = "King's Integration Property",
            latexFormula = "\\int_a^b f(x) \\, dx = \\int_a^b f(a + b - x) \\, dx",
            subject = Subject.MATHEMATICS,
            chapterName = "Definite Integrals",
            variables = listOf(
                "f(x) : \\text{Integrand continuous on } [a, b]",
                "a, b : \\text{Lower and upper limits of integration}"
            ),
            siUnits = "\\text{Dimensionless / Depends on application}",
            dimensionalFormula = "\\text{Analytical Identity}",
            crucialCaveats = "Does not require f(x) to be even or odd. If f(x) + f(a+b-x) = C (constant), then I = \\frac{C(b-a)}{2} instantly without evaluating antiderivative."
        )
    ,

        FormulaCard(
            id = "f_c11_thermo",
            name = "First Law of Thermodynamics",
            latexFormula = "\\Delta Q = \\Delta U + \\Delta W = n C_v \\Delta T + P \\Delta V",
            subject = Subject.PHYSICS,
            chapterName = "Thermodynamics",
            classGrade = 11,
            variables = listOf(
                "\\Delta Q : \text{Heat supplied}",
                "\\Delta U : \text{Change in internal energy}",
                "\\Delta W : \text{Work done by system}",
                "C_v : \text{Molar heat capacity at constant volume}"
            ),
            siUnits = "\text{Joules (J)}",
            dimensionalFormula = "[M L^2 T^{-2}]",
            crucialCaveats = "Internal energy U depends solely on temperature for an ideal gas. In an isothermal process (\\Delta T = 0), \\Delta U = 0, so all heat added turns into work (Q = W)."
        ),
        FormulaCard(
            id = "f_c11_carnot",
            name = "Carnot Engine Thermal Efficiency",
            latexFormula = "\\eta = 1 - \\frac{T_2}{T_1} = \\frac{W_{net}}{Q_1}",
            subject = Subject.PHYSICS,
            chapterName = "Thermodynamics",
            classGrade = 11,
            variables = listOf(
                "\\eta : \text{Thermal efficiency}",
                "T_1 : \text{Source temperature in KELVIN}",
                "T_2 : \text{Sink temperature in KELVIN}",
                "W_{net} : \text{Net output work}"
            ),
            siUnits = "\text{Dimensionless (fraction or %)}",
            dimensionalFormula = "[M^0 L^0 T^0]",
            crucialCaveats = "Temperatures MUST be expressed in Kelvin. Efficiency is strictly strictly less than 1 (100%) because sink temperature T_2 cannot reach absolute zero."
        ),
        FormulaCard(
            id = "f_c10_mirror",
            name = "Mirror Equation & Magnification",
            latexFormula = "\\frac{1}{f} = \\frac{1}{v} + \\frac{1}{u}, \\quad m = -\\frac{v}{u} = \\frac{h_i}{h_o}",
            subject = Subject.PHYSICS,
            chapterName = "Light: Reflection & Refraction",
            classGrade = 10,
            variables = listOf(
                "f : \text{Focal length (negative for concave, positive for convex)}",
                "u : \text{Object distance (always negative in standard sign convention)}",
                "v : \text{Image distance (negative for real, positive for virtual)}",
                "m : \text{Transverse magnification}"
            ),
            siUnits = "\text{Metres (m) or centimetres (cm)}",
            dimensionalFormula = "[L]",
            crucialCaveats = "In lens formula, the sign between terms is MINUS: 1/f = 1/v - 1/u, and lens magnification is POSITIVE m = +v/u."
        ),
        FormulaCard(
            id = "f_c9_force",
            name = "Newton's Second Law & Momentum",
            latexFormula = "\\vec{F} = \\frac{d\\vec{p}}{dt} = m \\vec{a} = m \\frac{\\vec{v} - \\vec{u}}{t}",
            subject = Subject.PHYSICS,
            chapterName = "Force & Laws of Motion",
            classGrade = 9,
            variables = listOf(
                "\\vec{F} : \text{Net applied external force}",
                "m : \text{Inertial mass in kg}",
                "\\vec{a} : \text{Acceleration in m/s}^2",
                "\\vec{p} : \text{Linear momentum (p = mv)}"
            ),
            siUnits = "\text{Newton (N) or kg} \\cdot \text{m/s}^2",
            dimensionalFormula = "[M L T^{-2}]",
            crucialCaveats = "F = ma holds only when mass m remains constant. If mass varies (like in a launching rocket), use F = dp/dt = m(dv/dt) + v(dm/dt)."
        ),
        FormulaCard(
            id = "f_c8_pressure",
            name = "Pressure & Thrust Relationship",
            latexFormula = "P = \\frac{F_{\\perp}}{A}",
            subject = Subject.PHYSICS,
            chapterName = "Force, Pressure & Sound Waves",
            classGrade = 8,
            variables = listOf(
                "P : \text{Pressure in Pascals (N/m}^2)",
                "F_{\\perp} : \text{Perpendicular thrust force}",
                "A : \text{Contact surface area in m}^2"
            ),
            siUnits = "\text{Pascal (Pa)}",
            dimensionalFormula = "[M L^{-1} T^{-2}]",
            crucialCaveats = "Pressure is inversely proportional to contact area. A sharp needle creates massive pressure with tiny force because tip area A is minuscule."
        ),
        FormulaCard(
            id = "f_c7_temp",
            name = "Temperature Scale Conversion",
            latexFormula = "\\frac{C}{5} = \\frac{F - 32}{9} = \\frac{K - 273.15}{5}",
            subject = Subject.CHEMISTRY,
            chapterName = "Heat, Temperature, Acids & Bases",
            classGrade = 7,
            variables = listOf(
                "C : \text{Temperature in degrees Celsius (°C)}",
                "F : \text{Temperature in degrees Fahrenheit (°F)}",
                "K : \text{Absolute temperature in Kelvin (K)}"
            ),
            siUnits = "\text{Kelvin (K) is the SI unit}",
            dimensionalFormula = "[\\Theta]",
            crucialCaveats = "At -40 degrees, both Celsius and Fahrenheit scales read identical numerical values: -40°C = -40°F."
        ),
        FormulaCard(
            id = "f_c6_shadow",
            name = "Rectilinear Shadow Length Relation",
            latexFormula = "L = h \\cdot \\cot \theta = \\frac{h}{\tan \theta}",
            subject = Subject.PHYSICS,
            chapterName = "Components of Food, Light & Shadows",
            classGrade = 6,
            variables = listOf(
                "L : \text{Shadow length on ground}",
                "h : \text{Height of opaque object}",
                "\theta : \text{Solar elevation angle above horizon}"
            ),
            siUnits = "\text{Metres (m)}",
            dimensionalFormula = "[L]",
            crucialCaveats = "At solar noon when the sun is overhead (theta near 90°), shadows are shortest. At sunrise and sunset (theta small), shadows stretch longest."
        ),
        FormulaCard(
            id = "f_c5_density",
            name = "Density of Matter",
            latexFormula = "\rho = \\frac{\text{Mass}}{\text{Volume}} = \\frac{m}{V}",
            subject = Subject.CHEMISTRY,
            chapterName = "States of Matter & Nutrition in Living Things",
            classGrade = 5,
            variables = listOf(
                "\rho : \text{Density}",
                "m : \text{Mass}",
                "V : \text{Volume occupied}"
            ),
            siUnits = "\text{kg/m}^3 \text{ or g/cm}^3",
            dimensionalFormula = "[M L^{-3}]",
            crucialCaveats = "An object floats in water if its density is less than 1 g/cm³. Ice floats on water because ice expands upon freezing, making it less dense than liquid water!"
        )

    )

    val practiceQuestions: List<PracticeQuestion> = listOf(
        PracticeQuestion(
            id = "q_es_1",
            chapterId = "phy_electrostatics",
            subject = Subject.PHYSICS,
            examType = ExamType.NEET,
            yearTag = "NEET 2023",
            questionText = "A parallel plate capacitor with air between the plates has a capacitance of 9 pF. The separation between its plates is 'd'. The space between the plates is now filled with two dielectrics. One of the dielectrics has dielectric constant K_1 = 3 and thickness d/3 while the other one has dielectric constant K_2 = 6 and thickness 2d/3. What is the new capacitance of the capacitor?",
            options = listOf(
                "18 pF",
                "20.25 pF",
                "40.5 pF",
                "9 pF"
            ),
            correctIndex = 1, // 20.25 pF
            trapOptionIndex = 0, // 18 pF (simple arithmetic average trap)
            trapReason = "Students often take arithmetic mean of K_1 and K_2 (4.5 \\times 9 = 40.5 pF) or treat them as parallel slabs instead of series capacitances!",
            stepByStepSolution = listOf(
                "1. Slabs of thickness t_1 = d/3 and t_2 = 2d/3 are stacked across the thickness, meaning they are arranged in SERIES.",
                "2. Capacitance with dielectric layers in series is given by: C = \\frac{\\varepsilon_0 A}{\\frac{t_1}{K_1} + \\frac{t_2}{K_2}}.",
                "3. Compute equivalent denominator: \\frac{d/3}{3} + \\frac{2d/3}{6} = \\frac{d}{9} + \\frac{d}{9} = \\frac{2d}{9}.",
                "4. Substitute into formula: C = \\frac{\\varepsilon_0 A}{2d/9} = \\frac{9}{2} \\cdot \\frac{\\varepsilon_0 A}{d} = 4.5 \\cdot C_0.",
                "5. Given C_0 = 9\\text{ pF} \\implies C = 4.5 \\times 9 = 20.25\\text{ pF}."
            ),
            ncertLineRef = "NCERT Class 12, Chapter 2, Page 78, Problem 2.9",
            difficulty = "Medium"
        ),
        PracticeQuestion(
            id = "q_chem_1",
            chapterId = "chem_equilibrium",
            subject = Subject.CHEMISTRY,
            examType = ExamType.NEET,
            yearTag = "NEET 2022",
            questionText = "For the reaction 2\\text{NO}_2(g) \\rightleftharpoons \\text{N}_2\\text{O}_4(g), the enthalpy change \\Delta H = -57.2\\text{ kJ/mol} (exothermic). Which of the following perturbations will increase the yield of dimerized product \\text{N}_2\\text{O}_4?",
            options = listOf(
                "Increasing temperature at constant pressure",
                "Adding an inert gas at constant pressure",
                "Decreasing the volume of the reaction container",
                "Adding a positive catalyst"
            ),
            correctIndex = 2, // Decreasing the volume
            trapOptionIndex = 1, // Adding inert gas at const pressure
            trapReason = "Adding inert gas at const pressure expands volume, shifting equilibrium to the side with MORE moles (reactants 2NO2), decreasing yield!",
            stepByStepSolution = listOf(
                "1. The reaction is: 2\\text{NO}_2(g) \\rightleftharpoons \\text{N}_2\\text{O}_4(g). Gaseous moles: \\Delta n_g = 1 - 2 = -1.",
                "2. Decreasing volume increases the total system pressure: P \\propto 1/V.",
                "3. According to Le Chatelier's principle, an increase in pressure shifts equilibrium toward fewer gaseous moles.",
                "4. Fewer moles is the product side (\text{N}_2\text{O}_4, 1 mole vs 2 moles of reactant).",
                "5. Hence, decreasing volume increases the forward yield of \\text{N}_2\\text{O}_4."
            ),
            ncertLineRef = "NCERT Class 11, Chapter 7, Page 207",
            difficulty = "Easy"
        ),
        PracticeQuestion(
            id = "q_bio_1",
            chapterId = "bio_genetics",
            subject = Subject.BIOLOGY,
            examType = ExamType.NEET,
            yearTag = "NEET 2024",
            questionText = "In the Lac Operon of Escherichia coli, what is the role of the product synthesized constitutively by the regulatory 'i' gene?",
            options = listOf(
                "Binds to the operator to repress transcription",
                "Binds to RNA polymerase to initiate transcription",
                "Acts as an allosteric enzyme to hydrolyze lactose into galactose",
                "Increases membrane permeability to allow \\beta-galactosides into the cytoplasm"
            ),
            correctIndex = 0,
            trapOptionIndex = 3,
            trapReason = "Permease is synthesized by the 'y' gene, NOT the 'i' gene! The 'i' stands for 'inhibitor' (repressor), not 'inducer'.",
            stepByStepSolution = listOf(
                "1. The regulatory 'i' gene derives its name from 'inhibitor'.",
                "2. It encodes the Lac repressor protein constitutively (active at all times).",
                "3. The repressor binds specifically to the operator (o) region adjacent to the promoter.",
                "4. Physical binding of the repressor sterically hinders RNA polymerase from transcribing structural genes z, y, a.",
                "5. When inducer (allolactose) is present, it binds the repressor, inactivating its DNA-binding capacity."
            ),
            ncertLineRef = "NCERT Class 12, Chapter 6, Page 116",
            difficulty = "Easy"
        ),
        PracticeQuestion(
            id = "q_math_1",
            chapterId = "math_calculus",
            subject = Subject.MATHEMATICS,
            examType = ExamType.JEE_MAIN,
            yearTag = "JEE Main 2024",
            questionText = "The value of the definite integral I = \\int_0^{\\pi/2} \\frac{\\sqrt{\\sin x}}{\\sqrt{\\sin x} + \\sqrt{\\cos x}} \\, dx is:",
            options = listOf(
                "\\pi / 2",
                "\\pi / 4",
                "\\pi / 8",
                "1"
            ),
            correctIndex = 1, // pi / 4
            trapOptionIndex = 0, // pi / 2
            trapReason = "Students forget that 2I = \\pi/2, so the answer must be divided by 2 to yield \\pi/4.",
            stepByStepSolution = listOf(
                "1. Given I = \\int_0^{\\pi/2} \\frac{\\sqrt{\\sin x}}{\\sqrt{\\sin x} + \\sqrt{\\cos x}} dx.",
                "2. Apply King's property: x \\mapsto \\frac{\\pi}{2} - x. Since \\sin(\\pi/2 - x) = \\cos x and \\cos(\\pi/2 - x) = \\sin x:",
                "3. I = \\int_0^{\\pi/2} \\frac{\\sqrt{\\cos x}}{\\sqrt{\\cos x} + \\sqrt{\\sin x}} dx.",
                "4. Add the two equations: 2I = \\int_0^{\\pi/2} \\frac{\\sqrt{\\sin x} + \\sqrt{\\cos x}}{\\sqrt{\\sin x} + \\sqrt{\\cos x}} dx = \\int_0^{\\pi/2} 1 \\, dx = \\frac{\\pi}{2}.",
                "5. Therefore, I = \\frac{\\pi}{4}."
            ),
            ncertLineRef = "NCERT Class 12, Chapter 7, Page 343, Example 35",
            difficulty = "Medium"
        ),
        PracticeQuestion(
            id = "q_c10_1",
            chapterId = "c10_light_optics",
            subject = Subject.PHYSICS,
            examType = ExamType.ALL,
            classGrade = 10,
            yearTag = "CBSE Class 10 Board 2023",
            questionText = "An object is placed at a distance of 20 cm in front of a concave mirror of focal length 15 cm. What is the distance and nature of the image formed?",
            options = listOf(
                "v = -60 cm, Real and Inverted",
                "v = +60 cm, Virtual and Erect",
                "v = -30 cm, Real and Inverted",
                "v = +30 cm, Virtual and Inverted"
            ),
            correctIndex = 0,
            trapOptionIndex = 1,
            trapReason = "Students forget that focal length of concave mirror is negative (f = -15 cm) and object distance is negative (u = -20 cm), causing them to miss the negative sign for real image distance v = -60 cm.",
            stepByStepSolution = listOf(
                "1. Sign convention: Concave mirror focal length f = -15 cm, Object distance u = -20 cm.",
                "2. Mirror formula: 1/f = 1/v + 1/u => 1/v = 1/f - 1/u.",
                "3. 1/v = 1/(-15) - 1/(-20) = -1/15 + 1/20 = (-4 + 3)/60 = -1/60.",
                "4. Therefore, v = -60 cm.",
                "5. Since v is negative, the image is formed in front of the mirror, meaning it is Real and Inverted."
            ),
            ncertLineRef = "NCERT Class 10, Chapter 10, Page 170",
            difficulty = "Medium"
        ),
        PracticeQuestion(
            id = "q_c9_1",
            chapterId = "c9_laws_motion",
            subject = Subject.PHYSICS,
            examType = ExamType.ALL,
            classGrade = 9,
            yearTag = "CBSE Class 9 Standard",
            questionText = "A constant force acts on an object of mass 5 kg for a duration of 2 seconds. It increases the object's velocity from 3 m/s to 7 m/s. What is the magnitude of the applied force?",
            options = listOf(
                "10 N",
                "20 N",
                "5 N",
                "2.5 N"
            ),
            correctIndex = 0,
            trapOptionIndex = 1,
            trapReason = "Students forget to divide by time delta t = 2s after computing change in momentum delta p = 20 kg m/s.",
            stepByStepSolution = listOf(
                "1. Given: Mass m = 5 kg, initial velocity u = 3 m/s, final velocity v = 7 m/s, time t = 2 s.",
                "2. Acceleration a = (v - u) / t = (7 - 3) / 2 = 4 / 2 = 2 m/s².",
                "3. Newton's 2nd Law: Force F = m * a = 5 kg * 2 m/s² = 10 N."
            ),
            ncertLineRef = "NCERT Class 9, Chapter 9, Page 120",
            difficulty = "Easy"
        ),
        PracticeQuestion(
            id = "q_c8_1",
            chapterId = "c8_force_sound",
            subject = Subject.PHYSICS,
            examType = ExamType.ALL,
            classGrade = 8,
            yearTag = "Class 8 Foundation Quiz",
            questionText = "Why do sound waves fail to travel from the Moon to the Earth across outer space?",
            options = listOf(
                "Sound is a mechanical wave requiring a material medium; space is a vacuum",
                "The Moon's gravity bends sound rays away from the Earth",
                "Sound waves travel too slowly and dissolve in sunlight",
                "Earth's atmosphere reflects 100% of cosmic sounds"
            ),
            correctIndex = 0,
            trapOptionIndex = 1,
            trapReason = "Students sometimes confuse sound waves with light rays bending under gravity.",
            stepByStepSolution = listOf(
                "1. Sound is a longitudinal mechanical compression wave.",
                "2. Mechanical waves require particle-to-particle collision to propagate.",
                "3. Outer space is a near-total vacuum with no air molecules.",
                "4. Hence, sound cannot travel through vacuum."
            ),
            ncertLineRef = "NCERT Class 8, Chapter 13",
            difficulty = "Easy"
        ),
        PracticeQuestion(
            id = "q_c5_1",
            chapterId = "c5_matter_life",
            subject = Subject.CHEMISTRY,
            examType = ExamType.ALL,
            classGrade = 5,
            yearTag = "Junior Science Olympiad",
            questionText = "When water vapor in the sky cools down and turns back into tiny liquid water droplets forming clouds, what is this process called?",
            options = listOf(
                "Condensation",
                "Evaporation",
                "Precipitation",
                "Transpiration"
            ),
            correctIndex = 0,
            trapOptionIndex = 2,
            trapReason = "Precipitation is when the water actually falls as rain or snow, whereas condensation is the change from gas to liquid inside the cloud.",
            stepByStepSolution = listOf(
                "1. Evaporation is liquid water turning into vapor by heating.",
                "2. As warm vapor rises and reaches cold high altitudes, it cools down.",
                "3. Gas turning back into liquid droplets upon cooling is Condensation.",
                "4. Millions of these tiny droplets coalesce into clouds."
            ),
            ncertLineRef = "Class 5 Environmental Science & Matter",
            difficulty = "Easy"
        )
    ,

        PracticeQuestion(
            id = "q_c11_1",
            chapterId = "c11_thermodynamics",
            subject = Subject.PHYSICS,
            examType = ExamType.ALL,
            classGrade = 11,
            yearTag = "JEE Main & NEET 2023",
            questionText = "An ideal gas is taken through a cyclic thermodynamic process A -> B -> C -> A. If process A -> B is isobaric at P = 400 kPa from V = 2 L to 6 L, B -> C is isochoric dropping to 100 kPa, and C -> A is a straight line on the P-V diagram returning to A, what is the net work done by the gas in one cycle?",
            options = listOf(
                "+600 Joules",
                "+1200 Joules",
                "-600 Joules",
                "+300 Joules"
            ),
            correctIndex = 0,
            trapOptionIndex = 1,
            trapReason = "Students forget that the enclosed area of a right-angled triangle is 1/2 * base * height, erroneously calculating rectangle base * height = (6-2) * (400-100) = 1200 J.",
            stepByStepSolution = listOf(
                "1. The cyclic path forms a right-angled triangle on the P-V indicator diagram.",
                "2. Base along volume axis: \\Delta V = 6\text{ L} - 2\text{ L} = 4\text{ L} = 4 \times 10^{-3}\text{ m}^3.",
                "3. Height along pressure axis: \\Delta P = 400\text{ kPa} - 100\text{ kPa} = 300\text{ kPa} = 300 \times 10^3\text{ Pa}.",
                "4. Enclosed area = \\frac{1}{2} \times \text{base} \times \text{height} = \\frac{1}{2} \times (4 \times 10^{-3}) \times (300 \times 10^3) = 600\text{ J}.",
                "5. Since the cycle is traversed in clockwise direction, net work is positive: +600 J."
            ),
            ncertLineRef = "NCERT Class 11 Physics, Chapter 12, Page 308",
            difficulty = "Medium"
        ),
        PracticeQuestion(
            id = "q_c11_2",
            chapterId = "c11_thermodynamics",
            subject = Subject.PHYSICS,
            examType = ExamType.ALL,
            classGrade = 11,
            yearTag = "NEET 2022",
            questionText = "A Carnot engine has an efficiency of 40% when its sink is at 300 K. By how much must the temperature of the source be increased to boost its efficiency to 50%?",
            options = listOf(
                "100 K",
                "50 K",
                "150 K",
                "200 K"
            ),
            correctIndex = 0,
            trapOptionIndex = 1,
            trapReason = "Students mistakenly take the direct difference in efficiencies (50% - 40% = 10%) and apply it linearly to 500 K instead of re-evaluating the new source temperature T1' = 600 K.",
            stepByStepSolution = listOf(
                "1. Carnot efficiency formula: \\eta = 1 - T_2 / T_1.",
                "2. For \\eta = 0.40 and T_2 = 300 K: 0.40 = 1 - 300 / T_1 \\implies 300 / T_1 = 0.60 \\implies T_1 = 500 K.",
                "3. For target \\eta' = 0.50: 0.50 = 1 - 300 / T_1' \\implies 300 / T_1' = 0.50 \\implies T_1' = 600 K.",
                "4. Increase in source temperature: \\Delta T_1 = T_1' - T_1 = 600 - 500 = 100 K."
            ),
            ncertLineRef = "NCERT Class 11 Physics, Chapter 12, Page 313",
            difficulty = "Medium"
        ),
        PracticeQuestion(
            id = "q_c7_1",
            chapterId = "c7_heat_acids",
            subject = Subject.CHEMISTRY,
            examType = ExamType.ALL,
            classGrade = 7,
            yearTag = "Class 7 Science Olympiad",
            questionText = "A student places a strip of blue litmus paper into an unknown solution 'X'. The paper immediately turns bright red. What is the nature of solution 'X'?",
            options = listOf(
                "Acidic",
                "Basic",
                "Neutral",
                "Saline water"
            ),
            correctIndex = 0,
            trapOptionIndex = 1,
            trapReason = "Students often mix up the colors: Acids turn Blue litmus Red (A-B-R rule), while Bases turn Red litmus Blue.",
            stepByStepSolution = listOf(
                "1. Litmus indicator changes color depending on pH.",
                "2. Acids donate hydrogen ions and turn blue litmus red.",
                "3. Bases turn red litmus blue.",
                "4. Since blue litmus turned red, solution X must be Acidic."
            ),
            ncertLineRef = "NCERT Class 7 Science, Chapter 5, Page 51",
            difficulty = "Easy"
        ),
        PracticeQuestion(
            id = "q_c6_1",
            chapterId = "c6_food_light",
            subject = Subject.PHYSICS,
            examType = ExamType.ALL,
            classGrade = 6,
            yearTag = "Class 6 Foundation Quiz",
            questionText = "Which of the following statements correctly describes the image formed by a pinhole camera?",
            options = listOf(
                "Real and inverted, preserving the object's natural colors",
                "Virtual and erect, showing only a dark shadow silhouette",
                "Real and upright, enlarged by glass refraction",
                "Virtual and magnified without color"
            ),
            correctIndex = 0,
            trapOptionIndex = 1,
            trapReason = "Students confuse a pinhole camera image with a shadow! Shadows are dark silhouettes, but pinhole images have full colors and details, flipped upside down.",
            stepByStepSolution = listOf(
                "1. Light travels in straight lines (rectilinear propagation).",
                "2. Rays crossing through the tiny aperture invert the image.",
                "3. The image is caught on an optical screen, making it REAL and INVERTED.",
                "4. All wavelengths of light pass through, so full colors are preserved."
            ),
            ncertLineRef = "NCERT Class 6 Science, Chapter 11, Page 110",
            difficulty = "Easy"
        ),
        PracticeQuestion(
            id = "q_c6_2",
            chapterId = "c6_food_light",
            subject = Subject.PHYSICS,
            examType = ExamType.ALL,
            classGrade = 6,
            yearTag = "Junior Science Olympiad",
            questionText = "When 2-3 drops of dilute iodine solution are added to a boiled potato paste, the color changes to:",
            options = listOf(
                "Blue-Black",
                "Bright Violet",
                "Brick Red",
                "Milky White"
            ),
            correctIndex = 0,
            trapOptionIndex = 1,
            trapReason = "Violet is the color obtained in the Biuret test for proteins (copper sulfate + sodium hydroxide), whereas iodine with starch turns blue-black.",
            stepByStepSolution = listOf(
                "1. Potatoes are rich in starch (a complex carbohydrate).",
                "2. The iodine test is the standard diagnostic test for starch.",
                "3. Iodine molecules trap inside the amylose coils of starch, forming a deep Blue-Black complex."
            ),
            ncertLineRef = "NCERT Class 6 Science, Chapter 2, Page 12",
            difficulty = "Easy"
        )

    )

    val flashcards: List<FlashcardItem> = listOf(
        FlashcardItem(
            id = "fc_1",
            front = "What is the electric field outside an infinite planar conducting plate with surface charge density \\sigma?",
            back = "E = \\frac{\\sigma}{\\varepsilon_0}. For a non-conducting dielectric thin sheet, it is E = \\frac{\\sigma}{2\\varepsilon_0}. Notice the factor of 2 difference due to double faces on conducting slab.",
            subject = Subject.PHYSICS,
            chapterName = "Electrostatics",
            formulaSnippet = "E = \\sigma / \\varepsilon_0"
        ),
        FlashcardItem(
            id = "fc_2",
            front = "State the thermodynamic condition when a reaction quotient Q is greater than K (Q > K).",
            back = "When Q > K, the reaction shifts in the REVERSE direction (consuming excess products to form reactants) until dynamic equilibrium is re-established.",
            subject = Subject.CHEMISTRY,
            chapterName = "Chemical Equilibrium",
            formulaSnippet = "Q > K \\implies \\text{Reverse}"
        ),
        FlashcardItem(
            id = "fc_3",
            front = "Which enzyme joins Okazaki fragments on the lagging strand during DNA replication?",
            back = "DNA Ligase catalyzes the formation of phosphodiester bonds between adjacent 3'-OH and 5'-phosphate nicks.",
            subject = Subject.BIOLOGY,
            chapterName = "Molecular Basis of Inheritance"
        ),
        FlashcardItem(
            id = "fc_4",
            front = "State King's Property of definite integrals.",
            back = "\\int_a^b f(x) dx = \\int_a^b f(a + b - x) dx. Adding I + I usually eliminates difficult trigonometric or logarithmic denominators.",
            subject = Subject.MATHEMATICS,
            chapterName = "Definite Integrals",
            formulaSnippet = "\\int_a^b f(x)dx = \\int_a^b f(a+b-x)dx"
        ),

        FlashcardItem(
            id = "fc_c11_1",
            front = "What is the difference in slope between an adiabatic and an isothermal curve on a P-V diagram?",
            back = "The adiabatic curve is steeper by a factor of \\gamma (ratio of specific heats): (dP/dV)_{adiabatic} = \\gamma (dP/dV)_{isothermal}, where \\gamma = C_p / C_v > 1.",
            subject = Subject.PHYSICS,
            chapterName = "Thermodynamics",
            classGrade = 11,
            formulaSnippet = "(dP/dV)_{ad} = \\gamma (dP/dV)_{iso}"
        ),
        FlashcardItem(
            id = "fc_c10_1",
            front = "Why is a convex mirror always used as a rear-view mirror in automobiles?",
            back = "A convex mirror always forms a virtual, erect, and diminished image, giving the driver a vastly wider field of view compared to a plane mirror.",
            subject = Subject.PHYSICS,
            chapterName = "Light: Reflection & Refraction",
            classGrade = 10,
            formulaSnippet = "m = +v/u < 1"
        ),
        FlashcardItem(
            id = "fc_c9_1",
            front = "Why do action and reaction forces in Newton's Third Law never cancel each other out?",
            back = "Because action and reaction act on TWO DIFFERENT BODIES simultaneously! Forces can only cancel if they act on the same physical object.",
            subject = Subject.PHYSICS,
            chapterName = "Force & Laws of Motion",
            classGrade = 9,
            formulaSnippet = "\\vec{F}_{AB} = -\\vec{F}_{BA}"
        ),
        FlashcardItem(
            id = "fc_c8_1",
            front = "What is the audible frequency range for normal human hearing?",
            back = "20 Hz to 20,000 Hz (20 kHz). Frequencies below 20 Hz are Infrasonic (elephants, earthquakes); frequencies above 20 kHz are Ultrasonic (bats, dolphins, ultrasound scanning).",
            subject = Subject.PHYSICS,
            chapterName = "Force, Pressure & Sound Waves",
            classGrade = 8,
            formulaSnippet = "20\text{ Hz} - 20\text{ kHz}"
        ),
        FlashcardItem(
            id = "fc_c7_1",
            front = "What products are formed in a chemical neutralization reaction?",
            back = "Acid + Base -> Salt + Water + Heat. The hydrogen ion (H+) from the acid combines with the hydroxide ion (OH-) from the base to form H2O.",
            subject = Subject.CHEMISTRY,
            chapterName = "Heat, Temperature, Acids & Bases",
            classGrade = 7,
            formulaSnippet = "\text{Acid} + \text{Base} \rightarrow \text{Salt} + \text{H}_2\text{O}"
        ),
        FlashcardItem(
            id = "fc_c6_1",
            front = "What is the principle behind a pinhole camera, and what type of image does it produce?",
            back = "It works on the rectilinear propagation of light (light travels in straight lines). It produces a REAL and INVERTED image that preserves the natural colors of the object.",
            subject = Subject.PHYSICS,
            chapterName = "Components of Food, Light & Shadows",
            classGrade = 6,
            formulaSnippet = "\text{Real & Inverted}"
        ),
        FlashcardItem(
            id = "fc_c5_1",
            front = "Why does ice float on liquid water?",
            back = "When water freezes below 4°C, its molecules form a rigid hexagonal crystalline cage that traps empty space, making ice LESS DENSE than liquid water (approx 0.92 g/cm³ vs 1.0 g/cm³).",
            subject = Subject.CHEMISTRY,
            chapterName = "States of Matter & Nutrition in Living Things",
            classGrade = 5,
            formulaSnippet = "\rho_{ice} < \rho_{water}"
        )

    )
}
